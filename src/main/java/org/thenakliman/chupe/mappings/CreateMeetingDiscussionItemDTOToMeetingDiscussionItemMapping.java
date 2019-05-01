package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.ConverterConfigurerSupport;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.CreateMeetingDiscussionItemDTO;
import org.thenakliman.chupe.models.Meeting;
import org.thenakliman.chupe.models.MeetingDiscussionItem;
import org.thenakliman.chupe.models.User;

@Component
public class CreateMeetingDiscussionItemDTOToMeetingDiscussionItemMapping
    extends ConverterConfigurerSupport<CreateMeetingDiscussionItemDTO, MeetingDiscussionItem> {

  private final DateUtil dateUtil;

  CreateMeetingDiscussionItemDTOToMeetingDiscussionItemMapping(DateUtil dateUtil) {
    this.dateUtil = dateUtil;
  }

  @Override
  public Converter<CreateMeetingDiscussionItemDTO, MeetingDiscussionItem> converter() {

    return new AbstractConverter<CreateMeetingDiscussionItemDTO, MeetingDiscussionItem>() {
      @Override
      protected MeetingDiscussionItem convert(CreateMeetingDiscussionItemDTO source) {
        return MeetingDiscussionItem
            .builder()
            .assignedTo(User.builder().userName(source.getAssignedTo()).build())
            .discussionItem(source.getDiscussionItem())
            .meeting(Meeting
                .builder()
                .id(source.getMeetingId())
                .build())
            .discussionItemType(source.getDiscussionItemType())
            .createdAt(dateUtil.getTime())
            .updatedAt(dateUtil.getTime())
            .build();
      }
    };
  }
}
