package org.thenakliman.chupe.services;

import java.util.Date;
import java.util.List;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.dto.TaskDTO;
import org.thenakliman.chupe.models.Task;
import org.thenakliman.chupe.repositories.TaskRepository;
import org.thenakliman.chupe.transformer.TaskTransformer;


@Service
public class TaskService {
  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private TaskTransformer taskTransformer;

  /** Should return all task.
   *
   * @return list of taskDTO
   * @throws NotFoundException when task does not exist
   */
  public List<TaskDTO> getAllTask() throws NotFoundException {
    List<Task> tasks = taskRepository.findAll();
    if (tasks.isEmpty()) {
      throw new NotFoundException("Tasks not found");
    }
    return taskTransformer.transformToListOfTaskDTO(tasks);
  }

  public TaskDTO saveTask(TaskDTO taskDTO) {
    Task task = taskTransformer.transformToTask(taskDTO);
    task.setCreatedAt(new Date());
    task.setUpdatedAt(new Date());
    Task savedTask = taskRepository.save(task);
    return taskTransformer.transformToTaskDTO(savedTask);
  }
}
