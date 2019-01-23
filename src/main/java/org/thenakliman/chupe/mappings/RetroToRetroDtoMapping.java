package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.PropertyMapConfigurerSupport;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.dto.RetroDTO;
import org.thenakliman.chupe.models.Retro;

@Component
public class RetroToRetroDtoMapping extends PropertyMapConfigurerSupport<Retro, RetroDTO> {

  @Override
  public PropertyMap<Retro, RetroDTO> mapping() {

    return new PropertyMap<Retro, RetroDTO>() {
      @Override
      protected void configure() {
        map().setCreatedBy(source.getCreatedBy().getUserName());
      }
    };
  }
}

