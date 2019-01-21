package org.thenakliman.chupe.services;

import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.TaskDTO;
import org.thenakliman.chupe.models.Task;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.TaskRepository;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.thenakliman.chupe.models.TaskState.*;


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

  public TaskDTO saveTask(TaskDTO taskDTO) {
    Task task = modelMapper.map(taskDTO, Task.class);
    task.setCreatedAt(dateUtil.getTime());
    task.setUpdatedAt(dateUtil.getTime());
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
    if (CREATED.equals(existingTask.getState())
        && (IN_PROGRESS.equals(updatedTask.getState()) || DONE.equals(updatedTask.getState()))) {

      existingTask.setStartOn(dateUtil.getTime());
    }

    if (!DONE.equals(existingTask.getState()) && DONE.equals(updatedTask.getState())) {
      existingTask.setEndedOn(dateUtil.getTime());
      existingTask.setProgress(100);
    }

    existingTask.setUpdatedAt(dateUtil.getTime());
    existingTask.setState(updatedTask.getState());
  }
}
