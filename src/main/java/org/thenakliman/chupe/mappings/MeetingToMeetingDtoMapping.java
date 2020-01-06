package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.PropertyMapConfigurerSupport;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.dto.MeetingDTO;
import org.thenakliman.chupe.models.Meeting;

@Component
public class MeetingToMeetingDtoMapping extends PropertyMapConfigurerSupport<Meeting, MeetingDTO> {

  @Override
  public PropertyMap<Meeting, MeetingDTO> mapping() {

    return new PropertyMap<>() {
      @Override
      protected void configure() {
        map().setCreatedBy(source.getCreatedBy().getUserName());
      }
    };
  }
}
