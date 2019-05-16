package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.ConverterConfigurerSupport;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.UpsertTaskDTO;
import org.thenakliman.chupe.models.Task;

@Component
public class UpsertTaskDtoToTaskMapping extends ConverterConfigurerSupport<UpsertTaskDTO, Task> {

  private final DateUtil dateUtil;

  UpsertTaskDtoToTaskMapping(DateUtil dateUtil) {
    this.dateUtil = dateUtil;
  }

  @Override
  public Converter<UpsertTaskDTO, Task> converter() {

    return new AbstractConverter<UpsertTaskDTO, Task>() {
      @Override
      protected Task convert(UpsertTaskDTO source) {
        return Task
            .builder()
            .description(source.getDescription())
            .state(source.getState())
            .progress(source.getProgress())
            .startOn(source.getStartedOn())
            .endedOn(source.getEndedOn())
            .createdAt(dateUtil.getTime())
            .updatedAt(dateUtil.getTime())
            .build();
      }
    };
  }
}
