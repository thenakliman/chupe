package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.ConverterConfigurerSupport;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.UpsertMeetingDiscussionItemDTO;
import org.thenakliman.chupe.models.*;

import java.util.Date;

import static java.util.Objects.isNull;

@Component
public class UpsertMeetingDiscussionItemDTOToMeetingDiscussionItemMapping
    extends ConverterConfigurerSupport<UpsertMeetingDiscussionItemDTO, MeetingDiscussionItem> {

  private final DateUtil dateUtil;

  UpsertMeetingDiscussionItemDTOToMeetingDiscussionItemMapping(DateUtil dateUtil) {
    this.dateUtil = dateUtil;
  }

  @Override
  public Converter<UpsertMeetingDiscussionItemDTO, MeetingDiscussionItem> converter() {

    return new AbstractConverter<>() {
      @Override
      protected MeetingDiscussionItem convert(UpsertMeetingDiscussionItemDTO source) {
        ActionItemStatus status = ActionItemStatus.DONE;
        if (DiscussionItemType.ACTION_ITEM.equals(source.getDiscussionItemType())) {
          status = ActionItemStatus.CREATED;
        }

        Date deadlineToAct = isNull(source.getDeadlineToAct()) ? dateUtil.getTime() : source.getDeadlineToAct();

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
            .deadlineToAct(deadlineToAct)
            .status(status)
            .build();
      }
    };
  }
}
