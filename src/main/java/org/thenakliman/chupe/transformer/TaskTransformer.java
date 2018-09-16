package org.thenakliman.chupe.transformer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.thenakliman.chupe.dto.TaskDTO;
import org.thenakliman.chupe.models.Task;


@Component
public class TaskTransformer {
  private TaskDTO transformToTaskDTO(Task task) {
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

  public List<TaskDTO> transformToListOfTaskDTO(List<Task> tasks) {
    return tasks.stream().map(this::transformToTaskDTO).collect(Collectors.toList());
  }
}
