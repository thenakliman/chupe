package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.ConverterConfigurerSupport;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.RetroDTO;
import org.thenakliman.chupe.models.Retro;
import org.thenakliman.chupe.models.User;

@Component
public class RetroDtoToRetroMapping extends ConverterConfigurerSupport<RetroDTO, Retro> {

  private DateUtil dateUtil;

  RetroDtoToRetroMapping(DateUtil dateUtil) {
    this.dateUtil = dateUtil;
  }

  @Override
  public Converter<RetroDTO, Retro> converter() {

    return new AbstractConverter<RetroDTO, Retro>() {
      @Override
      protected Retro convert(RetroDTO source) {
        User createdBy = User
            .builder()
            .userName(source.getCreatedBy())
            .build();

        return Retro
            .builder()
            .id(source.getId())
            .createdBy(createdBy)
            .name(source.getName())
            .maximumVote(source.getMaximumVote())
            .createdAt(dateUtil.getTime())
            .updatedAt(dateUtil.getTime())
            .build();
      }
    };
  }
}
