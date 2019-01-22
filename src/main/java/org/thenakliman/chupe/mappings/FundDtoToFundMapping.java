package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.ConverterConfigurerSupport;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.FundDTO;
import org.thenakliman.chupe.models.Fund;
import org.thenakliman.chupe.models.FundType;
import org.thenakliman.chupe.models.User;


@Component
public class FundDtoToFundMapping extends ConverterConfigurerSupport<FundDTO, Fund> {

  private DateUtil dateUtil;

  FundDtoToFundMapping(DateUtil dateUtil) {
    this.dateUtil = dateUtil;
  }

  @Override
  public Converter<FundDTO, Fund> converter() {

    return new AbstractConverter<FundDTO, Fund>() {
      @Override
      protected Fund convert(FundDTO source) {
        return Fund
            .builder()
            .transactionType(source.getTransactionType())
            .addedBy(getUser(source.getAddedBy()))
            .amount(source.getAmount())
            .createdAt(source.getCreatedAt())
            .id(source.getId())
            .isApproved(source.isApproved())
            .owner(getUser(source.getOwner()))
            .type(getFundType(source.getType()))
            .updatedAt(dateUtil.getTime())
            .build();
      }
    };
  }

  private FundType getFundType(Long type) {
    return FundType
        .builder()
        .id(type)
        .build();
  }

  private User getUser(String addedBy) {
    return User
        .builder()
        .userName(addedBy)
        .build();
  }
}
