package org.thenakliman.chupe.mappings;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.UpsertBestPracticeDTO;
import org.thenakliman.chupe.models.BestPractice;

@RunWith(MockitoJUnitRunner.class)
public class UpsertBestPracticeDtoToBestPracticeMappingTest {
  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private ModelMapper modelMapper;

  private Date now;

  @Before
  public void setUp() {
    now = new Date();
    when(dateUtil.getTime()).thenReturn(now);
    modelMapper.addConverter(new UpsertBestPracticeDtoToBestPracticeMapping(dateUtil).converter());
  }

  @Test
  public void shouldMapUpsertBestPracticeDtoToBestPractice() {
    UpsertBestPracticeDTO upsertBestPracticeDTO = UpsertBestPracticeDTO.builder()
        .description("hello world")
        .applicable(false)
        .doneWell("really great work")
        .needImprovement("need to work on some areas")
        .build();

    BestPractice bestPractice = modelMapper.map(upsertBestPracticeDTO, BestPractice.class);
    assertFalse(bestPractice.getApplicable());
    assertThat(bestPractice.getCreatedAt(), is(now));
    assertThat(bestPractice.getUpdatedAt(), is(now));
    assertNull(bestPractice.getCreatedBy());
    assertThat(bestPractice.getDescription(), is("hello world"));
    assertThat(bestPractice.getDoneWell(), is("really great work"));
    assertThat(bestPractice.getNeedImprovement(), is("need to work on some areas"));
  }
}