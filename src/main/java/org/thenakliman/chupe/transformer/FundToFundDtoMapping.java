package org.thenakliman.chupe.transformer;

import com.github.jmnarloch.spring.boot.modelmapper.PropertyMapConfigurerSupport;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.dto.FundDTO;
import org.thenakliman.chupe.models.Fund;

@Component
public class FundToFundDtoMapping extends PropertyMapConfigurerSupport<Fund, FundDTO> {

  @Override
  public PropertyMap<Fund, FundDTO> mapping() {

    return new PropertyMap<Fund, FundDTO>() {
      @Override
      protected void configure() {
        map().setType(source.getType().getId());
        map().setOwner(source.getOwner().getUserName());
        map().setAddedBy(source.getAddedBy().getUserName());
        map().setApproved(source.isApproved());
      }
    };
  }
}
