package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.PropertyMapConfigurerSupport;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.dto.ActionItemDTO;
import org.thenakliman.chupe.models.ActionItem;

@Component
public class ActionItemToActionItemDtoMapping extends PropertyMapConfigurerSupport<ActionItem, ActionItemDTO> {

  @Override
  public PropertyMap<ActionItem, ActionItemDTO> mapping() {

    return new PropertyMap<ActionItem, ActionItemDTO>() {
      @Override
      protected void configure() {
        map().setAssignedTo(source.getAssignedTo().getUserName());
        map().setCreatedBy(source.getCreatedBy().getUserName());
      }
    };
  }
}
