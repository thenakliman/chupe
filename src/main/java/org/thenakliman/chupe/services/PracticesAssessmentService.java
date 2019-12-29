package org.thenakliman.chupe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.BestPracticeAssessmentAnswerDTO;
import org.thenakliman.chupe.dto.BestPracticeAssessmentDTO;
import org.thenakliman.chupe.dto.BestPracticeDTO;
import org.thenakliman.chupe.exceptions.BadRequestException;
import org.thenakliman.chupe.exceptions.NotFoundException;
import org.thenakliman.chupe.models.PracticeAssessment;
import org.thenakliman.chupe.models.PracticeAssessmentAnswer;
import org.thenakliman.chupe.models.Retro;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.BestPracticeAssessmentRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class PracticesAssessmentService {
  private BestPracticeAssessmentRepository bestPracticeAssessmentRepository;
  private PracticeService practiceService;
  private DateUtil dateUtil;
  private Converter converter;

  @Autowired
  public PracticesAssessmentService(BestPracticeAssessmentRepository bestPracticeAssessmentRepository,
                                    PracticeService practiceService,
                                    DateUtil dateUtil,
                                    Converter converter) {
    this.bestPracticeAssessmentRepository = bestPracticeAssessmentRepository;
    this.practiceService = practiceService;
    this.dateUtil = dateUtil;
    this.converter = converter;
  }

  public BestPracticeAssessmentDTO saveBestPracticeAssessment(Long retroId, List<BestPracticeAssessmentAnswerDTO> assessmentAnswerDTOs, String currentUser) {
    throwExceptionIfInvalidPractice(assessmentAnswerDTOs);
    PracticeAssessment bestPracticeAssessment = constructAssessment(retroId, assessmentAnswerDTOs, currentUser);
    PracticeAssessment practiceAssessment = bestPracticeAssessmentRepository.save(bestPracticeAssessment);
    return constructAssessmentDto(practiceAssessment);
  }

  public List<BestPracticeAssessmentAnswerDTO> getPracticesAssessment(Long retroId, String username) {
    Optional<PracticeAssessment> practiceAssessmentOptional = bestPracticeAssessmentRepository.findByRetroIdAndAnsweredByUserName(retroId, username);
    PracticeAssessment practicesAssessment = practiceAssessmentOptional.orElseThrow(
            () -> new NotFoundException("Practices assessment not found for retro = " + retroId));
    return practicesAssessment.getPracticeAssessmentAnswers().stream()
            .map(practiceAssessment -> new BestPracticeAssessmentAnswerDTO(practiceAssessment.getBestPractice().getId(), practiceAssessment.getAnswer()))
            .collect(Collectors.toList());
  }

  private PracticeAssessment constructAssessment(Long retroId, List<BestPracticeAssessmentAnswerDTO> assessmentAnswerDTOS, String currentUser) {
    PracticeAssessment practiceAssessment = PracticeAssessment.builder()
            .answeredBy(User.builder().userName(currentUser).build())
            .retro(Retro.builder().id(retroId).build())
            .createdAt(dateUtil.getTime())
            .updatedAt(dateUtil.getTime())
            .build();

    List<PracticeAssessmentAnswer> practiceAssessmentAnswers = assessmentAnswerDTOS.stream()
            .map(bestPracticeAssessmentAnswerDTO -> converter.convertToObject(bestPracticeAssessmentAnswerDTO, PracticeAssessmentAnswer.class))
            .peek(bestPracticeAssessment -> bestPracticeAssessment.setPracticesAssessment(practiceAssessment))
            .collect(toList());

    practiceAssessment.setPracticeAssessmentAnswers(practiceAssessmentAnswers);
    return practiceAssessment;
  }

  private BestPracticeAssessmentDTO constructAssessmentDto(PracticeAssessment practiceAssessment) {
    List<BestPracticeAssessmentAnswerDTO> bestPracticesAssessmentAnsers = converter.convertToListOfObjects(
            practiceAssessment.getPracticeAssessmentAnswers(),
            BestPracticeAssessmentAnswerDTO.class);

    return BestPracticeAssessmentDTO.builder()
            .id(practiceAssessment.getId())
            .answers(bestPracticesAssessmentAnsers)
            .build();
  }

  private void throwExceptionIfInvalidPractice(List<BestPracticeAssessmentAnswerDTO> assessmentDTOs) {
    List<BestPracticeDTO> bestPractices = practiceService.getActiveBestPractices();
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
