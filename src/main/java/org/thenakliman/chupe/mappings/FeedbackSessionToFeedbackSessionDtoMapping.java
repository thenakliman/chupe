package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.ConverterConfigurerSupport;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.dto.FeedbackSessionDTO;
import org.thenakliman.chupe.models.FeedbackSession;

@Component
public class FeedbackSessionToFeedbackSessionDtoMapping
    extends ConverterConfigurerSupport<FeedbackSession, FeedbackSessionDTO> {

  @Override
  public Converter<FeedbackSession, FeedbackSessionDTO> converter() {

    return new AbstractConverter<>() {
      @Override
      protected FeedbackSessionDTO convert(FeedbackSession feedbackSession) {
        return FeedbackSessionDTO
            .builder()
            .id(feedbackSession.getId())
            .description(feedbackSession.getDescription())
            .build();
      }
    };
  }
}
