package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.ConverterConfigurerSupport;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.FeedbackSessionDTO;
import org.thenakliman.chupe.models.FeedbackSession;

@Component
public class FeedbackSessionDtoToFeedbackSessionMapping
    extends ConverterConfigurerSupport<FeedbackSessionDTO, FeedbackSession> {

  private final DateUtil dateUtil;

  FeedbackSessionDtoToFeedbackSessionMapping(DateUtil dateUtil) {
    this.dateUtil = dateUtil;
  }

  @Override
  public Converter<FeedbackSessionDTO, FeedbackSession> converter() {

    return new AbstractConverter<>() {
      @Override
      protected FeedbackSession convert(FeedbackSessionDTO feedbackSessionDTO) {
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
