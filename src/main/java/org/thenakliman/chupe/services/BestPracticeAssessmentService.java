package org.thenakliman.chupe.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.dto.BestPracticeAssessmentDTO;
import org.thenakliman.chupe.dto.BestPracticeDTO;
import org.thenakliman.chupe.dto.UpsertBestPracticeAssessmentDTO;
import org.thenakliman.chupe.exceptions.BadRequestException;
import org.thenakliman.chupe.exceptions.NotFoundException;
import org.thenakliman.chupe.models.BestPracticeAssessment;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.BestPracticeAssessmentRepository;

@Service
public class BestPracticeAssessmentService {
  private BestPracticeAssessmentRepository bestPracticeAssessmentRepository;
  private BestPracticeService bestPracticeService;
  private Converter converter;

  @Autowired
  public BestPracticeAssessmentService(BestPracticeAssessmentRepository bestPracticeAssessmentRepository,
                                       BestPracticeService bestPracticeService,
                                       Converter converter) {
    this.bestPracticeAssessmentRepository = bestPracticeAssessmentRepository;
    this.bestPracticeService = bestPracticeService;
    this.converter = converter;
  }

  public List<BestPracticeAssessmentDTO> saveBestPracticeAssessment(
      List<UpsertBestPracticeAssessmentDTO> upsertBestPracticeAssessmentDTOs, String currentUser) {

    validatePracticesIfInvalidThrowException(upsertBestPracticeAssessmentDTOs);

    final List<BestPracticeAssessment> assessments = upsertBestPracticeAssessmentDTOs.stream()
        .map(assessment -> converter.convertToObject(assessment, BestPracticeAssessment.class))
        .peek(bestPracticeAssessment -> bestPracticeAssessment.setAnsweredBy(User.builder().userName(currentUser).build()))
        .map(bestPracticeAssessment -> bestPracticeAssessmentRepository.save(bestPracticeAssessment))
        .collect(Collectors.toList());

    return converter.convertToListOfObjects(assessments, BestPracticeAssessmentDTO.class);
  }

  private void validatePracticesIfInvalidThrowException(List<UpsertBestPracticeAssessmentDTO> assessmentDTOs) {
    List<BestPracticeDTO> bestPractices = bestPracticeService.getActiveBestPractices();
    Set<Long> bestPracticesId = bestPractices.stream()
        .map(BestPracticeDTO::getId)
        .collect(Collectors.toSet());

    if (assessmentDTOs.size() != bestPracticesId.size()) {
      throw new BadRequestException(String.format(
          "All the assessments must be answered, valid assessments are %s, you have answered %s.",
          assessmentDTOs.size(),
          bestPracticesId.size()));
    }

    assessmentDTOs.stream()
        .filter(assessment -> !bestPracticesId.contains(assessment.getBestPracticeId()))
        .findAny()
        .ifPresent(assessment -> {
          throw new NotFoundException(String.format("Best practice %s does not exist", assessment.getBestPracticeId()));
        });
  }
}
