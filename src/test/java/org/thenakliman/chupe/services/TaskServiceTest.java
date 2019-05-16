package org.thenakliman.chupe.services;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.thenakliman.chupe.models.TaskState.CREATED;
import static org.thenakliman.chupe.models.TaskState.DONE;
import static org.thenakliman.chupe.models.TaskState.IN_PROGRESS;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.TaskDTO;
import org.thenakliman.chupe.dto.UpsertTaskDTO;
import org.thenakliman.chupe.exceptions.BadRequestException;
import org.thenakliman.chupe.exceptions.NotFoundException;
import org.thenakliman.chupe.models.Task;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.TaskRepository;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceTest {
  @Mock
  private TaskRepository taskRepository;

  @Mock
  private DateUtil dateUtil;

  @Mock
  private Converter converter;

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

  private UpsertTaskDTO getUpsertTaskDTO(String description) {
    return UpsertTaskDTO.builder()
        .description(description)
        .state(CREATED)
        .build();
  }

  @Test
  public void shouldFetchAllTasks() {
    String description = "description";
    String username = "username";
    Task task = getTask(description, username);
    when(taskRepository.findByCreatedByUserName(username)).thenReturn(singletonList(task));
    TaskDTO taskDTO = getTaskDTO(description, username);
    when(converter.convertToListOfObjects(singletonList(task), TaskDTO.class))
        .thenReturn(Collections.singletonList(taskDTO));

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
    when(converter.convertToObject(getUpsertTaskDTO(description), Task.class)).thenReturn(task);
    when(converter.convertToObject(task, TaskDTO.class)).thenReturn(taskDTO);

    TaskDTO actualTask = taskService.saveTask(getUpsertTaskDTO(description), username);

    assertThat(taskDTO, samePropertyValuesAs(actualTask));
  }

  @Test(expected = NotFoundException.class)
  public void shouldReturnNotFoundExceptionWhenIdDoesNotExist() {
    Long id = 101L;
    String username = "user-name";
    when(taskRepository.findByIdAndCreatedByUserName(id, username)).thenReturn(Optional.empty());
    taskService.updateTask(id, getUpsertTaskDTO("description"), username);
  }

  @Test(expected = BadRequestException.class)
  public void updateTask_shouldThrowBadRequest_whenChangeStateFromCreatedToDone() {
    Long id = 101L;
    String username = "user-name";
    Task existingTask = getTask("desc", username);
    when(taskRepository.findByIdAndCreatedByUserName(id, username)).thenReturn(Optional.of(existingTask));
    UpsertTaskDTO upsertTaskDTO = getUpsertTaskDTO("description");
    upsertTaskDTO.setState(DONE);
    taskService.updateTask(id, upsertTaskDTO, username);
  }

  @Test
  public void shouldReturnSetStartedOnWhenStateChangeToInProgress() {
    Long id = 101L;
    String description = "description";
    String username = "username";
    Task existingTask = getTask(description, username);
    Date date = new Date(1000);
    when(dateUtil.getTime()).thenReturn(date);
    when(taskRepository.findByIdAndCreatedByUserName(id, username)).thenReturn(Optional.of(existingTask));

    TaskDTO taskDTO = getTaskDTO(description, username);
    UpsertTaskDTO upsertTaskDTO = getUpsertTaskDTO(description);
    taskDTO.setState(IN_PROGRESS);
    upsertTaskDTO.setState(IN_PROGRESS);

    Task updatedTask = getTask(description, username);
    updatedTask.setState(IN_PROGRESS);
    when(taskRepository.save(any())).thenReturn(updatedTask);

    TaskDTO updatedTaskDTO = getTaskDTO(description, username);
    updatedTaskDTO.setState(IN_PROGRESS);
    when(converter.convertToObject(updatedTask, TaskDTO.class)).thenReturn(updatedTaskDTO);

    TaskDTO receivedTaskDTO = taskService.updateTask(id, upsertTaskDTO, username);

    assertThat(receivedTaskDTO, samePropertyValuesAs(updatedTaskDTO));
    verify(taskRepository).findByIdAndCreatedByUserName(id, username);
    verify(taskRepository).save(any());
    verify(converter).convertToObject(updatedTask, TaskDTO.class);
    verify(dateUtil, times(2)).getTime();
  }

  @Test
  public void shouldReturnSetEndedOnWhenStateChangedToDone() {
    Long id = 101L;
    String description = "description";
    String username = "username";
    Task existingTask = getTask(description, username);
    existingTask.setState(IN_PROGRESS);
    Date date = new Date(1000);
    when(dateUtil.getTime()).thenReturn(date);
    when(taskRepository.findByIdAndCreatedByUserName(id, username)).thenReturn(Optional.of(existingTask));
    UpsertTaskDTO upsertTaskDTO = getUpsertTaskDTO(description);
    upsertTaskDTO.setState(DONE);

    Task updatedTask = getTask(description, username);
    updatedTask.setState(DONE);
    when(taskRepository.save(any())).thenReturn(updatedTask);

    TaskDTO updatedTaskDTO = getTaskDTO(description, username);
    updatedTaskDTO.setState(DONE);
    when(converter.convertToObject(updatedTask, TaskDTO.class)).thenReturn(updatedTaskDTO);

    TaskDTO receivedTaskDTO = taskService.updateTask(id, upsertTaskDTO, username);

    assertThat(receivedTaskDTO, samePropertyValuesAs(updatedTaskDTO));
    verify(taskRepository).findByIdAndCreatedByUserName(id, username);
    verify(taskRepository).save(any());
    verify(converter).convertToObject(updatedTask, TaskDTO.class);
    verify(dateUtil, times(2)).getTime();
  }
}
