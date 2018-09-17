package org.thenakliman.chupe.transformer;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.dto.TaskDTO;
import org.thenakliman.chupe.models.Task;
import org.thenakliman.chupe.models.TaskState;
import org.thenakliman.chupe.models.User;


@RunWith(MockitoJUnitRunner.class)
public class TaskTransformerTest {

  @InjectMocks
  private TaskTransformer taskTransformer;

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
    expectedTaskDTO.setState(TaskState.CREATED);
    expectedTaskDTO.setDescription(description);
    expectedTaskDTO.setCreatedBy(username);
    return expectedTaskDTO;
  }

  @Test
  public void transformTaskToTaskDTO() {
    String description = "fakeDescription";
    String username = "FakeUser";
    List<Task> tasks = asList(getTask(description, username));
    List<TaskDTO> taskDTOs = taskTransformer.transformToListOfTaskDTO(tasks);
    assertThat(getTaskDTO(description, username), samePropertyValuesAs(taskDTOs.get(0)));
    assertThat(taskDTOs, hasSize(1));
  }

  @Test
  public void transformTaskDTOToTask() {
    String description = "fakeDescription";
    String username = "FakeUser";
    TaskDTO taskDTO = getTaskDTO(description, username);
    Task task = taskTransformer.transformToTask(taskDTO);
    Task expectedTask = getTask(description, username);

    assertEquals(expectedTask.getId(), task.getId());
    assertNotNull(task.getCreatedAt());
    assertThat(expectedTask.getCreatedBy(), samePropertyValuesAs(task.getCreatedBy()));
    assertEquals(expectedTask.getDescription(), task.getDescription());
    assertEquals(expectedTask.getProgress(), task.getProgress());
    assertEquals(expectedTask.getState(), task.getState());
    assertNotNull(task.getUpdatedAt());
  }
}