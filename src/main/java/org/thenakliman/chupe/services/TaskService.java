package org.thenakliman.chupe.services;

import static java.lang.String.format;
import static org.thenakliman.chupe.models.TaskState.CREATED;
import static org.thenakliman.chupe.models.TaskState.DONE;
import static org.thenakliman.chupe.models.TaskState.IN_PROGRESS;

import java.util.List;
import java.util.Optional;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.TaskDTO;
import org.thenakliman.chupe.models.Task;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.TaskRepository;
import org.thenakliman.chupe.transformer.TaskTransformer;


@Service
public class TaskService {
  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private TaskTransformer taskTransformer;
  
  @Autowired
  private DateUtil dateUtil;

  /** Should return all task.
   *
   * @return list of taskDTO
   * @throws NotFoundException when task does not exist
   */
  public List<TaskDTO> getAllTaskFor(String username) throws NotFoundException {
    User user = new User();
    user.setUserName(username);
    List<Task> tasks = taskRepository.findByCreatedBy(user);
    if (tasks.isEmpty()) {
      throw new NotFoundException("Tasks not found");
    }
    return taskTransformer.transformToListOfTaskDTO(tasks);
  }

  /** Save task.
   * @param taskDTO to be saved
   * @return TaskDTO of the saved task
   */
  public TaskDTO saveTask(TaskDTO taskDTO) {
    Task task = taskTransformer.transformToTask(taskDTO);
    task.setCreatedAt(dateUtil.getTime());
    task.setUpdatedAt(dateUtil.getTime());
    Task savedTask = taskRepository.save(task);
    return taskTransformer.transformToTaskDTO(savedTask);
  }

  /** Update task.
   * @param id of the task to be updated
   * @param taskDTO to be saved
   * @return TaskDTO of the saved task
   */
  public TaskDTO updateTask(Long id, TaskDTO taskDTO) throws NotFoundException {
    Optional<Task> task = taskRepository.findById(id);
    if (!task.isPresent()) {
      throw new NotFoundException(format("Task not found with id {}", id));
    }

    updateTaskAsPerUpdatePayload(task.get(), taskDTO);

    Task savedTask = taskRepository.save(task.get());
    return taskTransformer.transformToTaskDTO(savedTask);
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
