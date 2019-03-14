package org.thenakliman.chupe.services;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.thenakliman.chupe.models.TaskState.CREATED;
import static org.thenakliman.chupe.models.TaskState.DONE;
import static org.thenakliman.chupe.models.TaskState.IN_PROGRESS;

import java.util.List;
import java.util.Optional;

import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.TaskDTO;
import org.thenakliman.chupe.models.Task;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.TaskRepository;


@Service
public class TaskService {
  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private DateUtil dateUtil;

  public List<TaskDTO> getAllTask(String username) throws NotFoundException {
    User user = new User();
    user.setUserName(username);
    List<Task> tasks = taskRepository.findByCreatedBy(user);
    if (tasks.isEmpty()) {
      throw new NotFoundException("Tasks not found");
    }

    return tasks
        .stream()
        .map(task -> modelMapper.map(task, TaskDTO.class))
        .collect(toList());
  }

  public TaskDTO saveTask(TaskDTO taskDTO, String createdBy) {
    Task task = modelMapper.map(taskDTO, Task.class);
    task.setId(null);
    task.setCreatedBy(User.builder().userName(createdBy).build());
    Task savedTask = taskRepository.save(task);
    return modelMapper.map(savedTask, TaskDTO.class);
  }

  public TaskDTO updateTask(Long id, TaskDTO taskDTO) throws NotFoundException {
    Optional<Task> task = taskRepository.findById(id);
    if (!task.isPresent()) {
      throw new NotFoundException(format("Task not found with id %d", id));
    }

    updateTaskAsPerUpdatePayload(task.get(), taskDTO);

    Task savedTask = taskRepository.save(task.get());
    return modelMapper.map(savedTask, TaskDTO.class);
  }

  private void updateTaskAsPerUpdatePayload(Task existingTask, TaskDTO updatedTask) {
    if (taskStarted(existingTask, updatedTask)) {
      existingTask.setStartOn(dateUtil.getTime());
    }

    if (taskEnded(existingTask, updatedTask)) {
      existingTask.setEndedOn(dateUtil.getTime());
      existingTask.setProgress(100);
    }

    existingTask.setUpdatedAt(dateUtil.getTime());
    existingTask.setState(updatedTask.getState());
  }

  private boolean taskEnded(Task existingTask, TaskDTO updatedTask) {
    return !DONE.equals(existingTask.getState()) && DONE.equals(updatedTask.getState());
  }

  private boolean taskStarted(Task existingTask, TaskDTO updatedTask) {
    return taskInCreatedState(existingTask) && taskNextStateIsInProgressOrDone(updatedTask);
  }

  private boolean taskNextStateIsInProgressOrDone(TaskDTO updatedTask) {
    return IN_PROGRESS.equals(updatedTask.getState()) || DONE.equals(updatedTask.getState());
  }

  private boolean taskInCreatedState(Task existingTask) {
    return CREATED.equals(existingTask.getState());
  }
}
