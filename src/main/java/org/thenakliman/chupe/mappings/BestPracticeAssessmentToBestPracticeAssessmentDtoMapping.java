package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.ConverterConfigurerSupport;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.dto.BestPracticeAssessmentDTO;
import org.thenakliman.chupe.models.BestPracticeAssessment;

@Component
public class BestPracticeAssessmentToBestPracticeAssessmentDtoMapping
    extends ConverterConfigurerSupport<BestPracticeAssessment, BestPracticeAssessmentDTO> {

  @Override
  public Converter<BestPracticeAssessment, BestPracticeAssessmentDTO> converter() {

    return new AbstractConverter<>() {
      @Override
      protected BestPracticeAssessmentDTO convert(BestPracticeAssessment bestPracticeAssessment) {
        return BestPracticeAssessmentDTO.builder()
            .answer(bestPracticeAssessment.getAnswer())
            .bestPracticeId(bestPracticeAssessment.getBestPractice().getId())
            .retroId(bestPracticeAssessment.getRetro().getId())
            .id(bestPracticeAssessment.getId())
            .build();
      }
    };
  }
}
