package org.thenakliman.chupe.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.BestPracticeAssessmentAnswerDTO;
import org.thenakliman.chupe.dto.BestPracticeAssessmentDTO;
import org.thenakliman.chupe.dto.BestPracticeDTO;
import org.thenakliman.chupe.exceptions.BadRequestException;
import org.thenakliman.chupe.exceptions.NotFoundException;
import org.thenakliman.chupe.models.BestPracticeAssessment;
import org.thenakliman.chupe.models.BestPracticeAssessmentAnswer;
import org.thenakliman.chupe.models.Retro;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.BestPracticeAssessmentRepository;

@Service
public class BestPracticeAssessmentService {
  private BestPracticeAssessmentRepository bestPracticeAssessmentRepository;
  private BestPracticeService bestPracticeService;
  private DateUtil dateUtil;
  private Converter converter;

  @Autowired
  public BestPracticeAssessmentService(BestPracticeAssessmentRepository bestPracticeAssessmentRepository,
                                       BestPracticeService bestPracticeService,
                                       DateUtil dateUtil,
                                       Converter converter) {
    this.bestPracticeAssessmentRepository = bestPracticeAssessmentRepository;
    this.bestPracticeService = bestPracticeService;
    this.dateUtil = dateUtil;
    this.converter = converter;
  }

  public BestPracticeAssessmentDTO saveBestPracticeAssessment(Long retroId, List<BestPracticeAssessmentAnswerDTO> assessmentAnswerDTOs, String currentUser) {
    throwExceptionIfInvalidPractice(assessmentAnswerDTOs);
    BestPracticeAssessment bestPracticeAssessment = constructAssessment(retroId, assessmentAnswerDTOs, currentUser);
    BestPracticeAssessment practiceAssessment = bestPracticeAssessmentRepository.save(bestPracticeAssessment);
    return constructAssessmentDto(practiceAssessment);
  }

  private BestPracticeAssessment constructAssessment(Long retroId, List<BestPracticeAssessmentAnswerDTO> assessmentAnswerDTOS, String currentUser) {
    List<BestPracticeAssessmentAnswer> bestPracticeAssessmentAnswers = assessmentAnswerDTOS.stream()
        .map(bestPracticeAssessmentAnswerDTO -> converter.convertToObject(bestPracticeAssessmentAnswerDTO, BestPracticeAssessmentAnswer.class))
        .collect(Collectors.toList());

    return BestPracticeAssessment.builder()
        .answeredBy(User.builder().userName(currentUser).build())
        .retro(Retro.builder().id(retroId).build())
        .bestPracticeAssessmentAnswers(bestPracticeAssessmentAnswers)
        .createdAt(dateUtil.getTime())
        .updatedAt(dateUtil.getTime())
        .build();
  }

  private BestPracticeAssessmentDTO constructAssessmentDto(BestPracticeAssessment practiceAssessment) {
    List<BestPracticeAssessmentAnswerDTO> bestPracticesAssessmentAnsers = converter.convertToListOfObjects(
        practiceAssessment.getBestPracticeAssessmentAnswers(),
        BestPracticeAssessmentAnswerDTO.class);

    return BestPracticeAssessmentDTO.builder()
        .id(practiceAssessment.getId())
        .answers(bestPracticesAssessmentAnsers)
        .build();
  }

  private void throwExceptionIfInvalidPractice(List<BestPracticeAssessmentAnswerDTO> assessmentDTOs) {
    List<BestPracticeDTO> bestPractices = bestPracticeService.getActiveBestPractices();
    Set<Long> bestPracticesId = bestPractices.stream()
        .map(BestPracticeDTO::getId)
        .collect(Collectors.toSet());

    if (assessmentDTOs.size() != bestPracticesId.size()) {
      throw new BadRequestException(String.format(
          "Valid practices are: %s, you have answered: %s.", bestPracticesId.size(), assessmentDTOs.size()));
    }

    assessmentDTOs.stream()
        .filter(assessment -> !bestPracticesId.contains(assessment.getBestPracticeId()))
        .findAny()
        .ifPresent(assessment -> {
          throw new NotFoundException(String.format("Best practice %s does not exist", assessment.getBestPracticeId()));
        });
  }
}
