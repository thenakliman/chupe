package org.thenakliman.chupe.mappings;

import static java.util.Objects.isNull;

import com.github.jmnarloch.spring.boot.modelmapper.ConverterConfigurerSupport;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.UpsertQuestionDTO;
import org.thenakliman.chupe.models.Question;
import org.thenakliman.chupe.models.QuestionStatus;
import org.thenakliman.chupe.models.User;

@Component
public class UpsertQuestionDtoToQuestionMapping extends ConverterConfigurerSupport<UpsertQuestionDTO, Question> {

  private final DateUtil dateUtil;

  UpsertQuestionDtoToQuestionMapping(DateUtil dateUtil) {
    this.dateUtil = dateUtil;
  }

  @Override
  public Converter<UpsertQuestionDTO, Question> converter() {

    return new AbstractConverter<UpsertQuestionDTO, Question>() {
      @Override
      protected Question convert(UpsertQuestionDTO source) {
        QuestionStatus status = isNull(source.getStatus()) ? QuestionStatus.OPEN : source.getStatus();
        return Question
            .builder()
            .assignedTo(User.builder().userName(source.getAssignedTo()).build())
            .description(source.getDescription())
            .priority(source.getPriority())
            .status(status)
            .question(source.getQuestion())
            .createdAt(dateUtil.getTime())
            .updatedAt(dateUtil.getTime())
            .build();
      }
    };
  }
}
