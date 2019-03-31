package org.thenakliman.chupe.mappings;

import static org.junit.Assert.assertEquals;
import static org.thenakliman.chupe.models.TaskState.IN_PROGRESS;

import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.dto.TaskDTO;
import org.thenakliman.chupe.models.Task;
import org.thenakliman.chupe.models.TaskState;
import org.thenakliman.chupe.models.User;

@RunWith(MockitoJUnitRunner.class)
public class TaskToTaskDtoMappingTest {

  @InjectMocks
  private ModelMapper modelMapper;

  private Date now;

  @Before
  public void setUp() throws Exception {
    modelMapper.addMappings(new TaskToTaskDtoMapping().mapping());
    now = new Date();
  }

  @Test
  public void shouldMapTaskToTaskDTO() {
    String userName = "user-name";
    User user = User
        .builder()
        .userName(userName)
        .build();

    long taskId = 1001L;
    String description = "description";
    int progress = 10;
    TaskState status = IN_PROGRESS;
    Task task = Task
        .builder()
        .description(description)
        .createdBy(user)
        .endedOn(now)
        .id(taskId)
        .progress(progress)
        .startOn(now)
        .state(status)
        .build();

    TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);

    assertEquals(taskId, taskDTO.getId());
    assertEquals(description, taskDTO.getDescription());
    assertEquals(progress, taskDTO.getProgress());
    assertEquals(status, taskDTO.getState());
    assertEquals(userName, taskDTO.getCreatedBy());
    assertEquals(now, taskDTO.getEndedOn());
    assertEquals(now, taskDTO.getStartedOn());
  }
}