package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.ConverterConfigurerSupport;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.UpsertRetroDTO;
import org.thenakliman.chupe.models.Retro;
import org.thenakliman.chupe.models.RetroStatus;

@Component
public class UpsertRetroDtoToRetroMapping
    extends ConverterConfigurerSupport<UpsertRetroDTO, Retro> {

  private final DateUtil dateUtil;

  UpsertRetroDtoToRetroMapping(DateUtil dateUtil) {
    this.dateUtil = dateUtil;
  }

  @Override
  public Converter<UpsertRetroDTO, Retro> converter() {

    return new AbstractConverter<UpsertRetroDTO, Retro>() {
      @Override
      protected Retro convert(UpsertRetroDTO source) {
        return Retro
            .builder()
            .name(source.getName())
            .maximumVote(source.getMaximumVote())
            .createdAt(dateUtil.getTime())
            .updatedAt(dateUtil.getTime())
            .status(RetroStatus.OPEN)
            .build();
      }
    };
  }
}
