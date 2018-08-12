package org.thenakliman.chupe.services;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javassist.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.models.FundType;
import org.thenakliman.chupe.models.FundTypes;
import org.thenakliman.chupe.repositories.FundTypeRepository;
import org.thenakliman.chupe.repositories.TeamFundRepository;


@RunWith(MockitoJUnitRunner.class)
public class TeamFundServiceTest {
  @Mock
  private FundTypeRepository fundTypeRepository;

  @Mock
  private TeamFundRepository teamFundRepository;

  @InjectMocks
  private TeamFundService teamFundService;

  private List<FundType> getFundTypes() {
    FundType fundType = new FundType();
    fundType.setId(1);
    fundType.setDescription("description");
    fundType.setType(FundTypes.BIRTHDAY);
    List fundTypes = new ArrayList();
    fundTypes.add(fundType);
    return fundTypes;
  }

  @Test
  public void shouldReturnAllTeamFundType() throws NotFoundException {
    when(fundTypeRepository.findAll()).thenReturn(getFundTypes());
    List<FundType> fundTypes = teamFundService.getAllFundTypes();
    assertThat(getFundTypes(), samePropertyValuesAs(fundTypes));
  }

  @Test(expected = NotFoundException.class)
  public void shouldRaiseExceptionWhenFundTypeNotFound() throws NotFoundException {
    when(fundTypeRepository.findAll()).thenReturn(Collections.emptyList());
    teamFundService.getAllFundTypes();
  }
}