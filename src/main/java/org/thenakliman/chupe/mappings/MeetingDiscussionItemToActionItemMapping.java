package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.ConverterConfigurerSupport;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.ActionItem;
import org.thenakliman.chupe.dto.ActionItemType;
import org.thenakliman.chupe.models.MeetingDiscussionItem;

@Component
public class MeetingDiscussionItemToActionItemMapping
    extends ConverterConfigurerSupport<MeetingDiscussionItem, ActionItem> {

  private final DateUtil dateUtil;

  MeetingDiscussionItemToActionItemMapping(DateUtil dateUtil) {
    this.dateUtil = dateUtil;
  }

  @Override
  public Converter<MeetingDiscussionItem, ActionItem> converter() {

    return new AbstractConverter<>() {
      @Override
      protected ActionItem convert(MeetingDiscussionItem source) {
        return ActionItem
            .builder()
            .id(source.getId())
            .deadlineToAct(dateUtil.getTime())
            .description(source.getDiscussionItem())
            .type(ActionItemType.MEETING)
            .build();
      }
    };
  }

}
