package org.thenakliman.chupe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.TaskDTO;
import org.thenakliman.chupe.dto.UpsertTaskDTO;
import org.thenakliman.chupe.exceptions.BadRequestException;
import org.thenakliman.chupe.exceptions.NotFoundException;
import org.thenakliman.chupe.models.Task;
import org.thenakliman.chupe.models.TaskState;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.TaskRepository;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.thenakliman.chupe.models.TaskState.*;


@Service
public class TaskService {
  private TaskRepository taskRepository;
  private DateUtil dateUtil;
  private Converter converter;

  @Autowired
  public TaskService(TaskRepository taskRepository, DateUtil dateUtil, Converter converter) {
    this.taskRepository = taskRepository;
    this.dateUtil = dateUtil;
    this.converter = converter;
  }

  public List<TaskDTO> getAllTask(String username) {
    List<Task> tasks = taskRepository.findByCreatedByUserName(username);
    return converter.convertToListOfObjects(tasks, TaskDTO.class);
  }

  public TaskDTO saveTask(UpsertTaskDTO taskDTO, String createdBy) {
    Task task = converter.convertToObject(taskDTO, Task.class);
    task.setCreatedBy(User.builder().userName(createdBy).build());
    Task savedTask = taskRepository.save(task);
    return converter.convertToObject(savedTask, TaskDTO.class);
  }

  public TaskDTO updateTask(Long id, UpsertTaskDTO taskDTO, String createdBy) {
    Optional<Task> taskOptional = taskRepository.findByIdAndCreatedByUserName(id, createdBy);

    Task task = taskOptional.orElseThrow(() -> new NotFoundException(format("Task not found with id %d", id)));
    if (!task.getState().transitionPossible(taskDTO.getState())) {
      throw new BadRequestException(
          String.format("Not possible to change task status from %s to %s state",
              task.getState().name(),
              taskDTO.getState().name()));
    }

    updateTaskAsPerUpdatePayload(task, taskDTO);
    Task savedTask = taskRepository.save(task);
    return converter.convertToObject(savedTask, TaskDTO.class);
  }

  private void updateTaskAsPerUpdatePayload(Task existingTask, UpsertTaskDTO updatedTask) {
    if (taskStarted(existingTask.getState(), updatedTask.getState())) {
      existingTask.setStartOn(dateUtil.getTime());
    }

    if (taskEnded(existingTask.getState(), updatedTask.getState())) {
      existingTask.setEndedOn(dateUtil.getTime());
      existingTask.setProgress(100);
    }

    existingTask.setUpdatedAt(dateUtil.getTime());
    existingTask.setState(updatedTask.getState());
  }

  private boolean taskEnded(TaskState currentState, TaskState nextState) {
    return !DONE.equals(currentState) && DONE.equals(nextState);
  }

  private boolean taskStarted(TaskState currentState, TaskState nextState) {
    return CREATED.equals(currentState) && IN_PROGRESS.equals(nextState);
  }
}
