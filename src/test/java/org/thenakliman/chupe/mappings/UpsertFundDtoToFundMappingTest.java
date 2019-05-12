package org.thenakliman.chupe.mappings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.thenakliman.chupe.models.TransactionType.DEBIT;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.UpsertFundDTO;
import org.thenakliman.chupe.models.Fund;
import org.thenakliman.chupe.models.TransactionType;

@RunWith(MockitoJUnitRunner.class)
public class UpsertFundDtoToFundMappingTest {

  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private ModelMapper modelMapper;

  private Date now;

  @Before
  public void setUp() {
    now = new Date();
    modelMapper.addConverter(new UpsertFundDtoToFundMapping(dateUtil).converter());
    when(dateUtil.getTime()).thenReturn(now);
  }

  @Test
  public void shouldMapUpsertFundDtoToFund() {
    long amount = -1000L;
    int fundId = 7177;
    int fundType = 101;
    String addedBy = "added-by-user";
    String owner = "owner-user";
    TransactionType transactionType = DEBIT;
    UpsertFundDTO fundDto = UpsertFundDTO
        .builder()
        .amount(amount)
        .owner(owner)
        .transactionType(transactionType)
        .type(fundType)
        .build();

    Fund mappedFund = modelMapper.map(fundDto, Fund.class);

    assertNull(mappedFund.getAddedBy());
    assertEquals(-amount, mappedFund.getAmount());
    assertEquals(now, mappedFund.getCreatedAt());
    assertEquals(now, mappedFund.getUpdatedAt());
    assertNull(mappedFund.getId());
    assertEquals(owner, mappedFund.getOwner().getUserName());
    assertEquals(transactionType, mappedFund.getTransactionType());
    assertEquals(fundType, mappedFund.getType().getId());
  }
}