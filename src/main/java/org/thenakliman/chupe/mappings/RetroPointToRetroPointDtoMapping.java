package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.PropertyMapConfigurerSupport;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.dto.RetroPointDTO;
import org.thenakliman.chupe.models.RetroPoint;

@Component
public class RetroPointToRetroPointDtoMapping extends PropertyMapConfigurerSupport<RetroPoint, RetroPointDTO> {

  @Override
  public PropertyMap<RetroPoint, RetroPointDTO> mapping() {

    return new PropertyMap<RetroPoint, RetroPointDTO>() {
      @Override
      protected void configure() {
        map().setAddedBy(source.getAddedBy().getUserName());
      }
    };
  }
}
