package org.thenakliman.chupe.transformer;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.thenakliman.chupe.dto.TaskDTO;
import org.thenakliman.chupe.models.Task;
import org.thenakliman.chupe.models.User;


@Component
public class TaskTransformer {
  /** Transform Task to TaskDTO.
   *
   * @param task to be converted to dto
   * @return taskDTO
   */
  public TaskDTO transformToTaskDTO(Task task) {
    TaskDTO taskDTO = new TaskDTO();
    taskDTO.setId(task.getId());
    taskDTO.setDescription(task.getDescription());
    taskDTO.setProgress(task.getProgress());
    taskDTO.setState(task.getState());
    taskDTO.setCreatedBy(task.getCreatedBy().getUserName());
    taskDTO.setCreatedAt(task.getCreatedAt());
    taskDTO.setUpdatedAt(task.getUpdatedAt());
    return taskDTO;
  }

  /** transform TaskDTO to task.
   *
   * @param taskDTO to be converted to task
   * @return task
   */
  public Task transformToTask(TaskDTO taskDTO) {
    Task task = new Task();
    task.setId(taskDTO.getId());
    task.setDescription(taskDTO.getDescription());
    task.setProgress(taskDTO.getProgress());
    task.setState(taskDTO.getState());
    User user = new User();
    user.setUserName(taskDTO.getCreatedBy());
    task.setCreatedBy(user);
    task.setCreatedAt(taskDTO.getCreatedAt() == null ? new Date() : taskDTO.getCreatedAt());
    task.setUpdatedAt(taskDTO.getUpdatedAt() == null ? new Date() : taskDTO.getUpdatedAt());
    return task;
  }

  /** Convert list of taskDTO to tasks.
   *
   * @param tasks to be converted
   * @return TaskDTO
   */
  public List<TaskDTO> transformToListOfTaskDTO(List<Task> tasks) {
    return tasks.stream().map(this::transformToTaskDTO).collect(Collectors.toList());
  }
}
