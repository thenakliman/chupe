package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.ConverterConfigurerSupport;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.BestPracticeAssessmentDTO;
import org.thenakliman.chupe.dto.UpsertBestPracticeAssessmentDTO;
import org.thenakliman.chupe.models.BestPractice;
import org.thenakliman.chupe.models.BestPracticeAssessment;
import org.thenakliman.chupe.models.Retro;

@Component
public class BestPracticeAssessmentDtoToBestPracticeAssessmentMapping
    extends ConverterConfigurerSupport<UpsertBestPracticeAssessmentDTO, BestPracticeAssessment> {

  private final DateUtil dateUtil;

  BestPracticeAssessmentDtoToBestPracticeAssessmentMapping(DateUtil dateUtil) {
    this.dateUtil = dateUtil;
  }

  @Override
  public Converter<UpsertBestPracticeAssessmentDTO, BestPracticeAssessment> converter() {

    return new AbstractConverter<UpsertBestPracticeAssessmentDTO, BestPracticeAssessment>() {
      @Override
      protected BestPracticeAssessment convert(UpsertBestPracticeAssessmentDTO source) {
        return BestPracticeAssessment
            .builder()
            .retro(Retro.builder().id(source.getRetroId()).build())
            .answer(source.getAnswer())
            .bestPractice(BestPractice.builder().id(source.getBestPracticeId()).build())
            .createdAt(dateUtil.getTime())
            .updatedAt(dateUtil.getTime())
            .build();
      }
    };
  }
}
