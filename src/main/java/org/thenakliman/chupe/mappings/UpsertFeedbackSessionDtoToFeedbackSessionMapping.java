package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.ConverterConfigurerSupport;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.UpsertFeedbackSessionDTO;
import org.thenakliman.chupe.models.FeedbackSession;

@Component
public class UpsertFeedbackSessionDtoToFeedbackSessionMapping
    extends ConverterConfigurerSupport<UpsertFeedbackSessionDTO, FeedbackSession> {

  private final DateUtil dateUtil;

  UpsertFeedbackSessionDtoToFeedbackSessionMapping(DateUtil dateUtil) {
    this.dateUtil = dateUtil;
  }

  @Override
  public Converter<UpsertFeedbackSessionDTO, FeedbackSession> converter() {

    return new AbstractConverter<>() {
      @Override
      protected FeedbackSession convert(UpsertFeedbackSessionDTO feedbackSessionDTO) {
        return FeedbackSession
            .builder()
            .description(feedbackSessionDTO.getDescription())
            .createdAt(dateUtil.getTime())
            .updatedAt(dateUtil.getTime())
            .build();
      }
    };
  }
}
