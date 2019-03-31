package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.ConverterConfigurerSupport;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.UpsertRetroPointDTO;
import org.thenakliman.chupe.models.Retro;
import org.thenakliman.chupe.models.RetroPoint;

@Component
public class UpsertRetroPointDtoToRetroPointMapping
    extends ConverterConfigurerSupport<UpsertRetroPointDTO, RetroPoint> {

  private final DateUtil dateUtil;

  UpsertRetroPointDtoToRetroPointMapping(DateUtil dateUtil) {
    this.dateUtil = dateUtil;
  }

  @Override
  public Converter<UpsertRetroPointDTO, RetroPoint> converter() {

    return new AbstractConverter<>() {
      @Override
      protected RetroPoint convert(UpsertRetroPointDTO source) {
        return RetroPoint
            .builder()
            .description(source.getDescription())
            .retro(Retro.builder().id(source.getRetroId()).build())
            .type(source.getType())
            .createdAt(dateUtil.getTime())
            .updatedAt(dateUtil.getTime())
            .build();
      }
    };
  }
}
