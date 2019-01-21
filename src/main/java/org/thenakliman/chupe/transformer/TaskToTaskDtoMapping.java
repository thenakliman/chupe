package org.thenakliman.chupe.transformer;

import com.github.jmnarloch.spring.boot.modelmapper.PropertyMapConfigurerSupport;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.dto.TaskDTO;
import org.thenakliman.chupe.models.Task;

@Component
public class TaskToTaskDtoMapping extends PropertyMapConfigurerSupport<Task, TaskDTO> {

  @Override
  public PropertyMap<Task, TaskDTO> mapping() {

    return new PropertyMap<Task, TaskDTO>() {
      @Override
      protected void configure() {
        map().setCreatedBy(source.getCreatedBy().getUserName());
        map().setStartedOn(source.getStartOn());
      }
    };
  }
}
