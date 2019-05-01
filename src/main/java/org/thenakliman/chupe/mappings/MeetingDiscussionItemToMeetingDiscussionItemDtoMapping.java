package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.PropertyMapConfigurerSupport;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.dto.MeetingDTO;
import org.thenakliman.chupe.dto.MeetingDiscussionItemDTO;
import org.thenakliman.chupe.models.Meeting;
import org.thenakliman.chupe.models.MeetingDiscussionItem;

@Component
public class MeetingDiscussionItemToMeetingDiscussionItemDtoMapping
    extends PropertyMapConfigurerSupport<MeetingDiscussionItem, MeetingDiscussionItemDTO> {

  @Override
  public PropertyMap<MeetingDiscussionItem, MeetingDiscussionItemDTO> mapping() {

    return new PropertyMap<MeetingDiscussionItem, MeetingDiscussionItemDTO>() {
      @Override
      protected void configure() {
        map().setAssignedTo(source.getAssignedTo().getUserName());
        map().setCreatedBy(source.getCreatedBy().getUserName());
      }
    };
  }
}
