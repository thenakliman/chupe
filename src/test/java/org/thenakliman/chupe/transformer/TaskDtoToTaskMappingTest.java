package org.thenakliman.chupe.transformer;

import org.junit.Before;
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

import java.util.Date;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.thenakliman.chupe.models.TaskState.IN_PROGRESS;

@RunWith(MockitoJUnitRunner.class)
public class TaskDtoToTaskMappingTest {

  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private ModelMapper modelMapper;

  private Date now;

  @Before
  public void setUp() throws Exception {
    now = new Date();
    modelMapper.addConverter(new TaskDtoToTaskMapping(dateUtil).converter());
    when(dateUtil.getTime()).thenReturn(now);
  }

  @Test
  public void shouldMapTaskDtoToTask() {
    String userName = "user-name";
    long taskId = 1001L;
    String description = "description";
    TaskDTO taskDTO = TaskDTO
        .builder()
        .description(description)
        .createdBy(userName)
        .endedOn(now)
        .id(taskId)
        .progress(10)
        .startedOn(now)
        .state(IN_PROGRESS)
        .build();

    Task mappedTask = modelMapper.map(taskDTO, Task.class);

    User user = User
        .builder()
        .userName(userName)
        .build();

    Task task = Task
        .builder()
        .description(description)
        .createdBy(user)
        .endedOn(now)
        .id(taskId)
        .progress(10)
        .startOn(now)
        .state(IN_PROGRESS)
        .createdAt(now)
        .updatedAt(now)
        .build();

    assertThat(mappedTask, samePropertyValuesAs(task));
  }
}