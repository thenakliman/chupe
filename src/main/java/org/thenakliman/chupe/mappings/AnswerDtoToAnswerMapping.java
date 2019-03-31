package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.ConverterConfigurerSupport;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.AnswerDTO;
import org.thenakliman.chupe.models.Answer;
import org.thenakliman.chupe.models.User;

@Component
public class AnswerDtoToAnswerMapping extends ConverterConfigurerSupport<AnswerDTO, Answer> {

  private final DateUtil dateUtil;

  AnswerDtoToAnswerMapping(DateUtil dateUtil) {
    this.dateUtil = dateUtil;
  }

  @Override
  public Converter<AnswerDTO, Answer> converter() {

    return new AbstractConverter<AnswerDTO, Answer>() {
      @Override
      protected Answer convert(AnswerDTO source) {
        User answeredBy = User
            .builder()
            .userName(source.getAnsweredBy())
            .build();

        return Answer
            .builder()
            .answeredBy(answeredBy)
            .answer(source.getAnswer())
            .id(source.getId())
            .questionId(source.getQuestionId())
            .createdAt(dateUtil.getTime())
            .updatedAt(dateUtil.getTime())
            .build();
      }
    };
  }
}
