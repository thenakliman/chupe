package org.thenakliman.chupe.services;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.dto.BestPracticeAssessmentDTO;
import org.thenakliman.chupe.dto.BestPracticeDTO;
import org.thenakliman.chupe.dto.UpsertBestPracticeAssessmentDTO;
import org.thenakliman.chupe.exceptions.BadRequestException;
import org.thenakliman.chupe.exceptions.NotFoundException;
import org.thenakliman.chupe.models.BestPractice;
import org.thenakliman.chupe.models.BestPracticeAssessment;
import org.thenakliman.chupe.repositories.BestPracticeAssessmentRepository;

@RunWith(MockitoJUnitRunner.class)
public class BestPracticeAssessmentServiceTest {
  @Mock
  private BestPracticeAssessmentRepository bestPracticeAssessmentRepository;
  @Mock
  private BestPracticeService bestPracticeService;
  @Mock
  private Converter converter;
  @InjectMocks
  private BestPracticeAssessmentService bestPracticeAssessmentService;
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void saveBestPracticeAssessment_throwBadRequest_whenNumberOfBestPracticesAreNotEqualToApplicableAssessments() {
    when(bestPracticeService.getActiveBestPractices()).thenReturn(Collections.singletonList(BestPracticeDTO.builder().id(123L).build()));
    expectedException.expect(BadRequestException.class);
    bestPracticeAssessmentService.saveBestPracticeAssessment(Collections.emptyList(), "username");
  }

  @Test
  public void saveBestPracticeAssessment_throwBadRequest_someOfPracticesAreNotApplicable() {
    when(bestPracticeService.getActiveBestPractices()).thenReturn(Collections.singletonList(BestPracticeDTO.builder().id(123L).build()));
    expectedException.expect(NotFoundException.class);
    bestPracticeAssessmentService.saveBestPracticeAssessment(
        Collections.singletonList(UpsertBestPracticeAssessmentDTO.builder().bestPracticeId(12L).build()), "username");
  }

  @Test
  public void saveBestPracticeAssessment_savePractices() {
    when(bestPracticeService.getActiveBestPractices()).thenReturn(Collections.singletonList(BestPracticeDTO.builder().id(123L).build()));
    UpsertBestPracticeAssessmentDTO bestPracticeAssessmentDTO1 = UpsertBestPracticeAssessmentDTO.builder().bestPracticeId(123L).build();

    BestPracticeAssessment bestPracticeAssessment = BestPracticeAssessment.builder()
        .bestPractice(BestPractice.builder().description("some description").build())
        .build();

    when(converter.convertToObject(bestPracticeAssessmentDTO1, BestPracticeAssessment.class)).thenReturn(bestPracticeAssessment);
    when(bestPracticeAssessmentRepository.save(bestPracticeAssessment)).thenReturn(bestPracticeAssessment);
    BestPracticeAssessmentDTO bestPracticeAssessmentDTO = BestPracticeAssessmentDTO.builder().bestPracticeId(123L).build();
    when(converter.convertToListOfObjects(Collections.singletonList(bestPracticeAssessment), BestPracticeAssessmentDTO.class)).thenReturn(Collections.singletonList(bestPracticeAssessmentDTO));

    List<BestPracticeAssessmentDTO> bestPracticeAssessmentDTOs = bestPracticeAssessmentService.saveBestPracticeAssessment(
        Collections.singletonList(bestPracticeAssessmentDTO1), "username");

    assertThat(bestPracticeAssessmentDTOs, hasSize(1));
    assertThat(bestPracticeAssessmentDTOs, hasItems(bestPracticeAssessmentDTO));
  }
}