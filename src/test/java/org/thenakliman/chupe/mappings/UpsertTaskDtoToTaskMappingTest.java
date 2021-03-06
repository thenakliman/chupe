package org.thenakliman.chupe.mappings;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.thenakliman.chupe.models.TaskState.IN_PROGRESS;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.UpsertTaskDTO;
import org.thenakliman.chupe.models.Task;

@RunWith(MockitoJUnitRunner.class)
public class UpsertTaskDtoToTaskMappingTest {

  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private ModelMapper modelMapper;

  private Date now;

  @Before
  public void setUp() throws Exception {
    now = new Date();
    modelMapper.addConverter(new UpsertTaskDtoToTaskMapping(dateUtil).converter());
    when(dateUtil.getTime()).thenReturn(now);
  }

  @Test
  public void shouldMapTaskDtoToTask() {
    String description = "description";
    UpsertTaskDTO taskDTO = UpsertTaskDTO
        .builder()
        .description(description)
        .endedOn(now)
        .progress(10)
        .startedOn(now)
        .state(IN_PROGRESS)
        .build();

    Task mappedTask = modelMapper.map(taskDTO, Task.class);

    Task task = Task
        .builder()
        .description(description)
        .endedOn(now)
        .progress(10)
        .startOn(now)
        .state(IN_PROGRESS)
        .createdAt(now)
        .updatedAt(now)
        .build();

    assertThat(mappedTask, samePropertyValuesAs(task));
  }
}