package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.ConverterConfigurerSupport;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.RetroPointDTO;
import org.thenakliman.chupe.models.Retro;
import org.thenakliman.chupe.models.RetroPoint;
import org.thenakliman.chupe.models.User;

@Component
public class RetroPointDtoToRetroPointMapping extends
    ConverterConfigurerSupport<RetroPointDTO, RetroPoint> {

  private final DateUtil dateUtil;

  RetroPointDtoToRetroPointMapping(DateUtil dateUtil) {
    this.dateUtil = dateUtil;
  }

  @Override
  public Converter<RetroPointDTO, RetroPoint> converter() {

    return new AbstractConverter<RetroPointDTO, RetroPoint>() {
      @Override
      protected RetroPoint convert(RetroPointDTO source) {
        return RetroPoint
            .builder()
            .addedBy(getUser(source.getAddedBy()))
            .description(source.getDescription())
            .id(source.getId())
            .type(source.getType())
            .retro(getRetro(source.getRetroId()))
            .createdAt(dateUtil.getTime())
            .updatedAt(dateUtil.getTime())
            .build();
      }
    };
  }

  private Retro getRetro(Long retroId) {
    return Retro
        .builder()
        .id(retroId)
        .build();
  }

  private User getUser(String addedBy) {
    return User
        .builder()
        .userName(addedBy)
        .build();
  }
}
