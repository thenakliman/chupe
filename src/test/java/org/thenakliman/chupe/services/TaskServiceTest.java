package org.thenakliman.chupe.services;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import javassist.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.dto.TaskDTO;
import org.thenakliman.chupe.models.Task;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.TaskRepository;
import org.thenakliman.chupe.transformer.TaskTransformer;


@RunWith(MockitoJUnitRunner.class)
public class TaskServiceTest {
  @Mock
  private TaskRepository taskRepository;

  @Mock
  private TaskTransformer taskTransformer;

  @InjectMocks
  private TaskService taskService;

  private Task getTask(String description, String username) {
    Task task = new Task();
    task.setId(10L);
    task.setDescription(description);
    User createdBy = new User();
    createdBy.setUserName(username);
    task.setCreatedBy(createdBy);
    return task;
  }

  private TaskDTO getTaskDTO(String description, String username) {
    TaskDTO expectedTaskDTO = new TaskDTO();
    expectedTaskDTO.setId(10L);
    expectedTaskDTO.setDescription(description);
    expectedTaskDTO.setCreatedBy(username);
    return expectedTaskDTO;
  }

  @Test(expected = NotFoundException.class)
  public void shouldThrowErrorWhenTaskDoesNotExist() throws NotFoundException {
    when(taskRepository.findAll()).thenReturn(Collections.emptyList());
    taskService.getAllTask();
  }

  @Test
  public void shouldFetchAllTasks() throws NotFoundException {
    String description = "description";
    String username = "username";
    List<Task> tasks = asList(getTask(description, username));
    when(taskRepository.findAll()).thenReturn(tasks);
    List<TaskDTO> taskDTOs = asList(getTaskDTO(description, username));
    when(taskTransformer.transformToListOfTaskDTO(tasks)).thenReturn(taskDTOs);

    List<TaskDTO> taskDTOList = taskService.getAllTask();

    assertThat(taskDTOs.get(0), samePropertyValuesAs(taskDTOList.get(0)));
  }

  @Test
  public void shouldSaveTask() {
    String description = "description";
    String username = "username";
    Task task = getTask(description, username);
    when(taskRepository.save(task)).thenReturn(task);

    TaskDTO taskDTO = getTaskDTO(description, username);
    when(taskTransformer.transformToTask(taskDTO)).thenReturn(task);
    when(taskTransformer.transformToTaskDTO(task)).thenReturn(taskDTO);

    TaskDTO actualTask = taskService.saveTask(taskDTO);

    assertThat(taskDTO, samePropertyValuesAs(actualTask));
  }
}