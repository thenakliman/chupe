package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.ConverterConfigurerSupport;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.UpsertAnswerDTO;
import org.thenakliman.chupe.models.Answer;
import org.thenakliman.chupe.models.Question;

@Component
public class AnswerDtoToAnswerMapping extends ConverterConfigurerSupport<UpsertAnswerDTO, Answer> {

  private final DateUtil dateUtil;

  AnswerDtoToAnswerMapping(DateUtil dateUtil) {
    this.dateUtil = dateUtil;
  }

  @Override
  public Converter<UpsertAnswerDTO, Answer> converter() {

    return new AbstractConverter<UpsertAnswerDTO, Answer>() {
      @Override
      protected Answer convert(UpsertAnswerDTO source) {
        return Answer
            .builder()
            .answer(source.getAnswer())
            .question(Question.builder().id(source.getQuestionId()).build())
            .createdAt(dateUtil.getTime())
            .updatedAt(dateUtil.getTime())
            .build();
      }
    };
  }
}
