package org.thenakliman.chupe.mappings;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.dto.BestPracticeAssessmentDTO;
import org.thenakliman.chupe.models.BestPractice;
import org.thenakliman.chupe.models.BestPracticeAssessment;
import org.thenakliman.chupe.models.Retro;
import org.thenakliman.chupe.models.User;

@RunWith(MockitoJUnitRunner.class)
public class BestPracticeAssessmentToBestPracticeAssessmentDtoMappingTest {

  @InjectMocks
  private ModelMapper modelMapper;

  @Before
  public void setUp() throws Exception {
    modelMapper.addConverter(new BestPracticeAssessmentToBestPracticeAssessmentDtoMapping().converter());
  }

  @Test
  public void mapBestPracticeAssessmentToBestPracticeAssessmentDto() {
    BestPracticeAssessment bestPracticeAssessment = BestPracticeAssessment.builder()
        .bestPractice(BestPractice.builder().id(10L).build())
        .answer(true)
        .retro(Retro.builder().id(210L).build())
        .answeredBy(User.builder().userName("new user").build())
        .id(1111L)
        .build();

    BestPracticeAssessmentDTO bestPracticeAssessmentDTO = modelMapper.map(bestPracticeAssessment, BestPracticeAssessmentDTO.class);
    assertThat(bestPracticeAssessmentDTO.getBestPracticeId(), is(10L));
    assertThat(bestPracticeAssessmentDTO.getRetroId(), is(210L));
    assertThat(bestPracticeAssessmentDTO.getId(), is(1111L));
    assertTrue(bestPracticeAssessment.getAnswer());
  }
}