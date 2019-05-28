package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.PropertyMapConfigurerSupport;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.dto.ActionItem;
import org.thenakliman.chupe.dto.ActionItemType;
import org.thenakliman.chupe.models.RetroActionItem;

@Component
public class RetroActionItemToActionItemMapping extends PropertyMapConfigurerSupport<RetroActionItem, ActionItem> {

  @Override
  public PropertyMap<RetroActionItem, ActionItem> mapping() {

    return new PropertyMap<>() {
      @Override
      protected void configure() {
        map().setType(ActionItemType.RETRO_ACTION_ITEM);
      }
    };
  }
}
