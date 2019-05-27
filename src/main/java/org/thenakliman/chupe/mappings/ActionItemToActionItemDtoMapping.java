package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.PropertyMapConfigurerSupport;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.dto.ActionItemDTO;
import org.thenakliman.chupe.models.RetroActionItem;

@Component
public class ActionItemToActionItemDtoMapping extends PropertyMapConfigurerSupport<RetroActionItem, ActionItemDTO> {

  @Override
  public PropertyMap<RetroActionItem, ActionItemDTO> mapping() {

    return new PropertyMap<RetroActionItem, ActionItemDTO>() {
      @Override
      protected void configure() {
        map().setAssignedTo(source.getAssignedTo().getUserName());
        map().setCreatedBy(source.getCreatedBy().getUserName());
      }
    };
  }
}
