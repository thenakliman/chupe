package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.ConverterConfigurerSupport;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.TaskDTO;
import org.thenakliman.chupe.models.Task;
import org.thenakliman.chupe.models.User;

@Component
public class TaskDtoToTaskMapping extends ConverterConfigurerSupport<TaskDTO, Task> {

  private final DateUtil dateUtil;

  TaskDtoToTaskMapping(DateUtil dateUtil) {
    this.dateUtil = dateUtil;
  }

  @Override
  public Converter<TaskDTO, Task> converter() {

    return new AbstractConverter<TaskDTO, Task>() {
      @Override
      protected Task convert(TaskDTO source) {
        User answeredBy = User
            .builder()
            .userName(source.getCreatedBy())
            .build();

        return Task
            .builder()
            .id(source.getId())
            .description(source.getDescription())
            .state(source.getState())
            .progress(source.getProgress())
            .createdBy(answeredBy)
            .startOn(source.getStartedOn())
            .endedOn(source.getEndedOn())
            .createdAt(dateUtil.getTime())
            .updatedAt(dateUtil.getTime())
            .build();
      }
    };
  }
}
