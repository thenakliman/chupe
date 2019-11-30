package org.thenakliman.chupe.services;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.BestPracticeAssessmentAnswerDTO;
import org.thenakliman.chupe.dto.BestPracticeAssessmentDTO;
import org.thenakliman.chupe.dto.BestPracticeDTO;
import org.thenakliman.chupe.exceptions.BadRequestException;
import org.thenakliman.chupe.exceptions.NotFoundException;
import org.thenakliman.chupe.models.BestPractice;
import org.thenakliman.chupe.models.BestPracticeAssessment;
import org.thenakliman.chupe.models.BestPracticeAssessmentAnswer;
import org.thenakliman.chupe.models.Retro;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.BestPracticeAssessmentRepository;

@RunWith(MockitoJUnitRunner.class)
public class BestPracticeAssessmentServiceTest {
  @Mock
  private BestPracticeAssessmentRepository bestPracticeAssessmentRepository;
  @Mock
  private BestPracticeService bestPracticeService;
  @Mock
  private Converter converter;
  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private BestPracticeAssessmentService bestPracticeAssessmentService;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void saveBestPracticeAssessment_throwBadRequest_whenNumberOfBestPracticesAreNotEqualToApplicableAssessments() {
    when(bestPracticeService.getActiveBestPractices()).thenReturn(singletonList(BestPracticeDTO.builder().id(123L).build()));
    expectedException.expect(BadRequestException.class);
    bestPracticeAssessmentService.saveBestPracticeAssessment(123L, Collections.emptyList(), "username");
  }

  @Test
  public void saveBestPracticeAssessment_throwBadRequest_someOfPracticesAreNotApplicable() {
    when(bestPracticeService.getActiveBestPractices()).thenReturn(singletonList(BestPracticeDTO.builder().id(123L).build()));
    expectedException.expect(NotFoundException.class);
    bestPracticeAssessmentService.saveBestPracticeAssessment(
        1234567L,
        singletonList(BestPracticeAssessmentAnswerDTO.builder().bestPracticeId(12L).build()),
        "username");
  }

  @Test
  public void saveBestPracticeAssessment_savePractices() {
    long bestPracticeId = 12345L;
    when(bestPracticeService.getActiveBestPractices()).thenReturn(singletonList(BestPracticeDTO.builder().id(bestPracticeId).build()));

    Long retroId = 12345678L;
    BestPracticeAssessmentAnswerDTO bestPracticeAssessmentAnswerDTO = BestPracticeAssessmentAnswerDTO.builder().bestPracticeId(bestPracticeId).build();
    BestPracticeAssessmentAnswer practiceAssessmentAnswer = BestPracticeAssessmentAnswer.builder().bestPractice(BestPractice.builder().id(bestPracticeId).build()).build();
    when(converter.convertToObject(bestPracticeAssessmentAnswerDTO, BestPracticeAssessmentAnswer.class)).thenReturn(practiceAssessmentAnswer);

    Date now = new Date();
    when(dateUtil.getTime()).thenReturn(now);
    BestPracticeAssessment bestPracticeAssessment = BestPracticeAssessment.builder()
        .bestPracticeAssessmentAnswers(singletonList(practiceAssessmentAnswer))
        .answeredBy(User.builder().userName("username").build())
        .retro(Retro.builder().id(retroId).build())
        .createdAt(now)
        .updatedAt(now)
        .build();
    when(bestPracticeAssessmentRepository.save(bestPracticeAssessment)).thenReturn(bestPracticeAssessment);
    when(converter.convertToListOfObjects(singletonList(practiceAssessmentAnswer), BestPracticeAssessmentAnswerDTO.class))
        .thenReturn(singletonList(bestPracticeAssessmentAnswerDTO));

    BestPracticeAssessmentDTO practiceAssessmentDTO = bestPracticeAssessmentService.saveBestPracticeAssessment(retroId, singletonList(bestPracticeAssessmentAnswerDTO), "username");
    assertThat(practiceAssessmentDTO.getAnswers(), hasSize(1));
    assertThat(practiceAssessmentDTO.getAnswers(), hasItem(bestPracticeAssessmentAnswerDTO));
  }
}