package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.ConverterConfigurerSupport;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.UpsertBestPracticeDTO;
import org.thenakliman.chupe.models.BestPractice;

@Component
public class UpsertBestPracticeDtoToBestPracticeMapping extends ConverterConfigurerSupport<UpsertBestPracticeDTO, BestPractice> {

  private final DateUtil dateUtil;

  UpsertBestPracticeDtoToBestPracticeMapping(DateUtil dateUtil) {
    this.dateUtil = dateUtil;
  }

  @Override
  public Converter<UpsertBestPracticeDTO, BestPractice> converter() {

    return new AbstractConverter<UpsertBestPracticeDTO, BestPractice>() {
      @Override
      protected BestPractice convert(UpsertBestPracticeDTO source) {
        return BestPractice
            .builder()
            .description(source.getDescription())
            .applicable(source.isApplicable())
            .createdAt(dateUtil.getTime())
            .updatedAt(dateUtil.getTime())
            .build();
      }
    };
  }
}
