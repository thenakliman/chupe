package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.ConverterConfigurerSupport;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.BestPracticeAssessmentAnswerDTO;
import org.thenakliman.chupe.models.BestPractice;
import org.thenakliman.chupe.models.PracticeAssessmentAnswer;

@Component
public class BestPracticeAssessmentAnswerDtoToBestPracticeAssessmentAnswerMapping
    extends ConverterConfigurerSupport<BestPracticeAssessmentAnswerDTO, PracticeAssessmentAnswer> {

  private final DateUtil dateUtil;

  @Autowired
  public BestPracticeAssessmentAnswerDtoToBestPracticeAssessmentAnswerMapping(DateUtil dateUtil) {
    this.dateUtil = dateUtil;
  }

  @Override
  public Converter<BestPracticeAssessmentAnswerDTO, PracticeAssessmentAnswer> converter() {
    return new AbstractConverter<>() {
      @Override
      protected PracticeAssessmentAnswer convert(BestPracticeAssessmentAnswerDTO bestPracticeAssessmentAnswer) {
        return PracticeAssessmentAnswer.builder()
            .bestPractice(BestPractice.builder().id(bestPracticeAssessmentAnswer.getBestPracticeId()).build())
            .answer(bestPracticeAssessmentAnswer.getAnswer())
            .createdAt(dateUtil.getTime())
            .updatedAt(dateUtil.getTime())
            .build();
      }
    };
  }
}
