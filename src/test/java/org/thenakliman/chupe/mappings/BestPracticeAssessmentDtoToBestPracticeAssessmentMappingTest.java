package org.thenakliman.chupe.mappings;

import static org.hamcrest.Matchers.is;
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
import org.thenakliman.chupe.dto.UpsertBestPracticeAssessmentDTO;
import org.thenakliman.chupe.models.BestPracticeAssessment;

@RunWith(MockitoJUnitRunner.class)
public class BestPracticeAssessmentDtoToBestPracticeAssessmentMappingTest {

  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private ModelMapper modelMapper;

  private Date now;

  @Before
  public void setUp() {
    now = new Date();
    when(dateUtil.getTime()).thenReturn(now);
    modelMapper.addConverter(new BestPracticeAssessmentDtoToBestPracticeAssessmentMapping(dateUtil).converter());
  }

  @Test
  public void shouldMapFeedbackSessionDtoToFeedbackSession() {
    UpsertBestPracticeAssessmentDTO upsertBestPracticeAssessmentDTO = UpsertBestPracticeAssessmentDTO.builder()
        .bestPracticeId(123L)
        .retroId(223L)
        .answer(false)
        .build();

    BestPracticeAssessment bestPracticeAssessment = modelMapper.map(upsertBestPracticeAssessmentDTO, BestPracticeAssessment.class);
    assertThat(bestPracticeAssessment.getAnswer(), is(false));
    assertThat(bestPracticeAssessment.getRetro().getId(), is(223L));
    assertThat(bestPracticeAssessment.getBestPractice().getId(), is(123L));
    assertThat(bestPracticeAssessment.getCreatedAt(), is(now));
    assertThat(bestPracticeAssessment.getUpdatedAt(), is(now));
    assertNull(bestPracticeAssessment.getAnsweredBy());
  }
}