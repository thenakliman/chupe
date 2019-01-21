package org.thenakliman.chupe.services;

import javassist.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.TaskDTO;
import org.thenakliman.chupe.models.Task;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.TaskRepository;
import org.thenakliman.chupe.transformer.TaskTransformer;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.thenakliman.chupe.models.TaskState.*;


@RunWith(MockitoJUnitRunner.class)
public class TaskServiceTest {
  @Mock
  private TaskRepository taskRepository;

  @Mock
  private ModelMapper modelMapper;

  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private TaskService taskService;

  private Task getTask(String description, String username) {
    Task task = new Task();
    task.setId(10L);
    task.setDescription(description);
    User createdBy = new User();
    createdBy.setUserName(username);
    task.setCreatedBy(createdBy);
    task.setState(CREATED);
    return task;
  }

  private TaskDTO getTaskDTO(String description, String username) {
    return TaskDTO.builder()
        .id(10L)
        .description(description)
        .createdBy(username)
        .state(CREATED).build();
  }

  @Test(expected = NotFoundException.class)
  public void shouldThrowErrorWhenTaskDoesNotExist() throws NotFoundException {
    User user = new User();
    user.setUserName("username");

    when(taskRepository.findByCreatedBy(any(User.class))).thenReturn(Collections.emptyList());
    taskService.getAllTask("username");
  }

  @Test
  public void shouldFetchAllTasks() throws NotFoundException {
    String description = "description";
    String username = "username";
    Task task = getTask(description, username);
    when(taskRepository.findByCreatedBy(any(User.class))).thenReturn(singletonList(task));
    TaskDTO taskDTO = getTaskDTO(description, username);
    when(modelMapper.map(task, TaskDTO.class)).thenReturn(taskDTO);

    List<TaskDTO> taskDTOList = taskService.getAllTask(username);

    assertThat(taskDTOList, hasItems(taskDTO));
    assertThat(taskDTOList, hasSize(1));
  }

  @Test
  public void shouldSaveTask() {
    String description = "description";
    String username = "username";
    Task task = getTask(description, username);
    when(taskRepository.save(task)).thenReturn(task);

    TaskDTO taskDTO = getTaskDTO(description, username);
    when(modelMapper.map(taskDTO, Task.class)).thenReturn(task);
    when(modelMapper.map(task, TaskDTO.class)).thenReturn(taskDTO);

    TaskDTO actualTask = taskService.saveTask(taskDTO);

    assertThat(taskDTO, samePropertyValuesAs(actualTask));
  }

  @Test(expected = NotFoundException.class)
  public void shouldReturnNotFoundExceptionWhenIdDoesNotExist() throws NotFoundException {
    Long id = 101L;
    when(taskRepository.findById(id)).thenReturn(Optional.empty());
    taskService.updateTask(id, getTaskDTO("description", "username"));
  }

  @Test
  public void shouldReturnSetStartedOnWhenStateChangeToInProgress() throws NotFoundException {
    Long id = 101L;
    String description = "description";
    String username = "username";
    Task existingTask = getTask(description, username);
    Date date = new Date(1000);
    when(dateUtil.getTime()).thenReturn(date);
    when(taskRepository.findById(id)).thenReturn(Optional.of(existingTask));

    TaskDTO taskDTO = getTaskDTO(description, username);
    taskDTO.setState(IN_PROGRESS);

    Task updatedTask = getTask(description, username);
    updatedTask.setState(IN_PROGRESS);
    when(taskRepository.save(any())).thenReturn(updatedTask);

    TaskDTO updatedTaskDTO = getTaskDTO(description, username);
    updatedTaskDTO.setState(IN_PROGRESS);
    when(modelMapper.map(updatedTask, TaskDTO.class)).thenReturn(updatedTaskDTO);

    TaskDTO receivedTaskDTO = taskService.updateTask(id, taskDTO);

    assertThat(receivedTaskDTO, samePropertyValuesAs(updatedTaskDTO));
    verify(taskRepository).findById(id);
    verify(taskRepository).save(any());
    verify(modelMapper).map(updatedTask, TaskDTO.class);
    verify(dateUtil, times(2)).getTime();
  }

  @Test
  public void shouldReturnSetStartedOnWhenStateChangedToDone() throws NotFoundException {
    Long id = 101L;
    String description = "description";
    String username = "username";
    Task existingTask = getTask(description, username);
    Date date = new Date(1000);
    when(dateUtil.getTime()).thenReturn(date);
    when(taskRepository.findById(id)).thenReturn(Optional.of(existingTask));
    TaskDTO taskDTO = getTaskDTO(description, username);
    taskDTO.setState(DONE);

    Task updatedTask = getTask(description, username);
    updatedTask.setState(DONE);
    when(taskRepository.save(any())).thenReturn(updatedTask);

    TaskDTO updatedTaskDTO = getTaskDTO(description, username);
    updatedTaskDTO.setState(DONE);
    when(modelMapper.map(updatedTask, TaskDTO.class)).thenReturn(updatedTaskDTO);

    TaskDTO receivedTaskDTO = taskService.updateTask(id, taskDTO);

    assertThat(receivedTaskDTO, samePropertyValuesAs(updatedTaskDTO));
    verify(taskRepository).findById(id);
    verify(taskRepository).save(any());
    verify(modelMapper).map(updatedTask, TaskDTO.class);
    verify(dateUtil, times(3)).getTime();
  }

  @Test
  public void shouldReturnSetStartedOnWhenStateChangedToOnHold() throws NotFoundException {
    Long id = 101L;
    String description = "description";
    String username = "username";
    Task existingTask = getTask(description, username);
    Date date = new Date(1000);
    when(dateUtil.getTime()).thenReturn(date);
    when(taskRepository.findById(id)).thenReturn(Optional.of(existingTask));

    TaskDTO taskDTO = getTaskDTO(description, username);
    taskDTO.setState(ON_HOLD);

    Task updatedTask = getTask(description, username);
    updatedTask.setState(ON_HOLD);
    when(taskRepository.save(any())).thenReturn(updatedTask);

    TaskDTO updatedTaskDTO = getTaskDTO(description, username);
    updatedTaskDTO.setState(ON_HOLD);
    when(modelMapper.map(updatedTask, TaskDTO.class)).thenReturn(updatedTaskDTO);

    TaskDTO receivedTaskDTO = taskService.updateTask(id, taskDTO);

    assertThat(receivedTaskDTO, samePropertyValuesAs(updatedTaskDTO));
    verify(taskRepository).findById(id);
    verify(taskRepository).save(any());
    verify(modelMapper).map(updatedTask, TaskDTO.class);
    verify(dateUtil, times(1)).getTime();
  }
}
