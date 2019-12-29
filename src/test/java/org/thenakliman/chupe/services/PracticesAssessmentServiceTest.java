package org.thenakliman.chupe.services;

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
import org.thenakliman.chupe.models.*;
import org.thenakliman.chupe.repositories.BestPracticeAssessmentRepository;

import java.util.*;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PracticesAssessmentServiceTest {
  @Mock
  private BestPracticeAssessmentRepository bestPracticeAssessmentRepository;
  @Mock
  private PracticeService practiceService;
  @Mock
  private Converter converter;
  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private PracticesAssessmentService practicesAssessmentService;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void saveBestPracticeAssessment_throwBadRequest_whenNumberOfBestPracticesAreNotEqualToApplicableAssessments() {
    when(practiceService.getActiveBestPractices()).thenReturn(singletonList(BestPracticeDTO.builder().id(123L).build()));
    expectedException.expect(BadRequestException.class);
    practicesAssessmentService.saveBestPracticeAssessment(123L, Collections.emptyList(), "username");
  }

  @Test
  public void saveBestPracticeAssessment_throwBadRequest_someOfPracticesAreNotApplicable() {
    when(practiceService.getActiveBestPractices()).thenReturn(singletonList(BestPracticeDTO.builder().id(123L).build()));
    expectedException.expect(NotFoundException.class);
    practicesAssessmentService.saveBestPracticeAssessment(
            1234567L,
            singletonList(BestPracticeAssessmentAnswerDTO.builder().bestPracticeId(12L).build()),
            "username");
  }

  @Test
  public void saveBestPracticeAssessment_savePractices() {
    long bestPracticeId = 12345L;
    when(practiceService.getActiveBestPractices()).thenReturn(singletonList(BestPracticeDTO.builder().id(bestPracticeId).build()));

    Long retroId = 12345678L;
    BestPracticeAssessmentAnswerDTO bestPracticeAssessmentAnswerDTO = BestPracticeAssessmentAnswerDTO.builder().bestPracticeId(bestPracticeId).build();
    PracticeAssessmentAnswer practiceAssessmentAnswer = PracticeAssessmentAnswer.builder().bestPractice(BestPractice.builder().id(bestPracticeId).build()).build();
    when(converter.convertToObject(bestPracticeAssessmentAnswerDTO, PracticeAssessmentAnswer.class)).thenReturn(practiceAssessmentAnswer);

    Date now = new Date();
    when(dateUtil.getTime()).thenReturn(now);
    PracticeAssessment practiceAssessment = PracticeAssessment.builder()
            .practiceAssessmentAnswers(singletonList(practiceAssessmentAnswer))
            .answeredBy(User.builder().userName("username").build())
            .retro(Retro.builder().id(retroId).build())
            .createdAt(now)
            .updatedAt(now)
            .build();
    when(bestPracticeAssessmentRepository.save(practiceAssessment)).thenReturn(practiceAssessment);
    when(converter.convertToListOfObjects(singletonList(practiceAssessmentAnswer), BestPracticeAssessmentAnswerDTO.class))
            .thenReturn(singletonList(bestPracticeAssessmentAnswerDTO));

    BestPracticeAssessmentDTO practiceAssessmentDTO = practicesAssessmentService.saveBestPracticeAssessment(retroId, singletonList(bestPracticeAssessmentAnswerDTO), "username");
    assertThat(practiceAssessmentDTO.getAnswers(), hasSize(1));
    assertThat(practiceAssessmentDTO.getAnswers(), hasItem(bestPracticeAssessmentAnswerDTO));
  }

  @Test
  public void getPracticesAssessment_throwExceptionIfAssessmentDoesNotExist() {
    Long retroId = 123L;
    String username = "user";
    when(bestPracticeAssessmentRepository.findByRetroIdAndAnsweredByUserName(retroId, username)).thenReturn(Optional.empty());
    expectedException.expect(NotFoundException.class);
    practicesAssessmentService.getPracticesAssessment(retroId, username);
  }

  @Test
  public void getPracticesAssessment_returnListOfPracticeAssessmentAnswer() {
    Long retroId = 123L;
    String username = "user";
    PracticeAssessment practiceAssessment = PracticeAssessment.builder()
            .practiceAssessmentAnswers(Arrays.asList(
                    PracticeAssessmentAnswer.builder().bestPractice(BestPractice.builder().id(10L).build()).answer(false).build(),
                    PracticeAssessmentAnswer.builder().bestPractice(BestPractice.builder().id(101L).build()).answer(true).build()
            ))
            .build();
    when(bestPracticeAssessmentRepository.findByRetroIdAndAnsweredByUserName(retroId, username)).thenReturn(Optional.of(practiceAssessment));
    List<BestPracticeAssessmentAnswerDTO> practicesAssessment = practicesAssessmentService.getPracticesAssessment(retroId, username);
    assertThat(practicesAssessment, hasItems(
            new BestPracticeAssessmentAnswerDTO(10L, false),
            new BestPracticeAssessmentAnswerDTO(101L, true)));
  }
}