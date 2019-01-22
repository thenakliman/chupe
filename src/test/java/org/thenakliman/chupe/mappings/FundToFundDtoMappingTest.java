package org.thenakliman.chupe.mappings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.dto.FundDTO;
import org.thenakliman.chupe.models.Fund;
import org.thenakliman.chupe.models.FundType;
import org.thenakliman.chupe.models.TransactionType;
import org.thenakliman.chupe.models.User;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.thenakliman.chupe.models.TransactionType.CREDIT;

@RunWith(MockitoJUnitRunner.class)
public class FundToFundDtoMappingTest {

  @InjectMocks
  private ModelMapper modelMapper;

  private Date now;
  @Before
  public void setUp() throws Exception {
    modelMapper.addMappings(new FundToFundDtoMapping().mapping());
    now = new Date();
  }

  @Test
  public void shouldMapAnswerToAnswerDTO() {
    String ownerUserName = "owner-name";
    User owner = getUser(ownerUserName);
    String addedUserName = "added-by-name";
    User addedBy = getUser(addedUserName);
    long fundTypeId = 101L;
    FundType fundType = FundType
        .builder()
        .id(fundTypeId)
        .type("type")
        .build();

    long fundId = 1001L;
    long amount = 10L;
    TransactionType transactionType = CREDIT;
    Fund fund = Fund
        .builder()
        .addedBy(addedBy)
        .amount(amount)
        .createdAt(now)
        .id(fundId)
        .isApproved(true)
        .owner(owner)
        .type(fundType)
        .updatedAt(now)
        .transactionType(transactionType)
        .build();

    FundDTO fundDTO = modelMapper.map(fund, FundDTO.class);

    assertEquals(fundId, fundDTO.getId());
    assertEquals(addedUserName, fundDTO.getAddedBy());
    assertEquals(amount, fundDTO.getAmount());
    assertEquals(fundTypeId, fundDTO.getType());
    assertEquals(now, fundDTO.getCreatedAt());
    assertEquals(ownerUserName, fundDTO.getOwner());
    assertEquals(transactionType, fundDTO.getTransactionType());
    assertTrue(fundDTO.isApproved());
  }

  private User getUser(String userName) {
    return User
          .builder()
          .userName(userName)
          .build();
  }
}