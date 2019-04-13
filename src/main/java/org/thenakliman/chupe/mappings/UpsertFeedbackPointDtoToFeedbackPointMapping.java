package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.ConverterConfigurerSupport;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.UpsertFeedbackPointDTO;
import org.thenakliman.chupe.models.FeedbackPoint;
import org.thenakliman.chupe.models.FeedbackSession;
import org.thenakliman.chupe.models.User;

@Component
public class UpsertFeedbackPointDtoToFeedbackPointMapping
    extends ConverterConfigurerSupport<UpsertFeedbackPointDTO, FeedbackPoint> {

  private final DateUtil dateUtil;

  UpsertFeedbackPointDtoToFeedbackPointMapping(DateUtil dateUtil) {
    this.dateUtil = dateUtil;
  }

  @Override
  public Converter<UpsertFeedbackPointDTO, FeedbackPoint> converter() {

    return new AbstractConverter<>() {
      @Override
      protected FeedbackPoint convert(UpsertFeedbackPointDTO feedbackPointDTO) {
        return FeedbackPoint
            .builder()
            .description(feedbackPointDTO.getDescription())
            .givenTo(User.builder().userName(feedbackPointDTO.getGivenTo()).build())
            .feedbackSession(FeedbackSession.builder().id(feedbackPointDTO.getSessionId()).build())
            .createdAt(dateUtil.getTime())
            .updatedAt(dateUtil.getTime())
            .build();
      }
    };
  }
}
