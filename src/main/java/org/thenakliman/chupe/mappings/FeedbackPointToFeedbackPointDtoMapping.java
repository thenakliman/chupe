package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.ConverterConfigurerSupport;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.dto.FeedbackPointDTO;
import org.thenakliman.chupe.models.FeedbackPoint;

@Component
public class FeedbackPointToFeedbackPointDtoMapping
    extends ConverterConfigurerSupport<FeedbackPoint, FeedbackPointDTO> {

  @Override
  public Converter<FeedbackPoint, FeedbackPointDTO> converter() {

    return new AbstractConverter<>() {
      @Override
      protected FeedbackPointDTO convert(FeedbackPoint feedbackPoint) {
        return FeedbackPointDTO
            .builder()
            .id(feedbackPoint.getId())
            .sessionId(feedbackPoint.getFeedbackSession().getId())
            .description(feedbackPoint.getDescription())
            .givenTo(feedbackPoint.getGivenTo().getUserName())
            .givenBy(feedbackPoint.getGivenBy().getUserName())
            .build();
      }
    };
  }
}
