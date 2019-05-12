package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.ConverterConfigurerSupport;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.UpsertFundDTO;
import org.thenakliman.chupe.models.Fund;
import org.thenakliman.chupe.models.FundType;
import org.thenakliman.chupe.models.User;


@Component
public class UpsertFundDtoToFundMapping extends ConverterConfigurerSupport<UpsertFundDTO, Fund> {

  private final DateUtil dateUtil;

  @Autowired
  UpsertFundDtoToFundMapping(DateUtil dateUtil) {
    this.dateUtil = dateUtil;
  }

  @Override
  public Converter<UpsertFundDTO, Fund> converter() {

    return new AbstractConverter<>() {
      @Override
      protected Fund convert(UpsertFundDTO source) {
        return Fund
            .builder()
            .transactionType(source.getTransactionType())
            .amount(Math.abs(source.getAmount()))
            .owner(getUser(source.getOwner()))
            .type(getFundType(source.getType()))
            .updatedAt(dateUtil.getTime())
            .createdAt(dateUtil.getTime())
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
