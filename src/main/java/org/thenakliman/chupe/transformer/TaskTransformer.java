package org.thenakliman.chupe.transformer;

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
    return TaskDTO.builder()
        .id(task.getId())
        .description(task.getDescription())
        .progress(task.getProgress())
        .state(task.getState())
        .createdBy(task.getCreatedBy().getUserName())
        .startedOn(task.getStartOn())
        .endedOn(task.getEndedOn()).build();
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
