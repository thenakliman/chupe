package org.thenakliman.chupe.services;

import java.util.List;
import java.util.Optional;

import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.ConverterUtil;
import org.thenakliman.chupe.dto.QuestionDTO;
import org.thenakliman.chupe.models.Question;
import org.thenakliman.chupe.repositories.QuestionRepository;


@Service
public class QuestionService {

  @Autowired
  private QuestionRepository questionsRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private ConverterUtil converterUtil;

  public QuestionDTO addQuestion(Question question) {
    return modelMapper.map(questionsRepository.save(question), QuestionDTO.class);
  }

  public List<QuestionDTO> getQuestions() {
    List<Question> questions = questionsRepository.findAll();
    return converterUtil.convertToListOfObjects(questions, QuestionDTO.class);
  }

  public void updateQuestions(long id, QuestionDTO question) throws NotFoundException {
    Optional<Question> savedQuestion = questionsRepository.findById(id);

    if (!savedQuestion.isPresent()) {
      throw new NotFoundException(String.format("Question %s not found", id));
    }

    savedQuestion.ifPresent(existingQuestion -> {
      existingQuestion.setAssignedTo(question.getAssignedTo());
      existingQuestion.setDescription(question.getDescription());
      existingQuestion.setOwner(question.getOwner());
      existingQuestion.setQuestion(question.getQuestion());
      existingQuestion.setStatus(question.getStatus());
      existingQuestion.setPriority(question.getPriority());
      questionsRepository.save(existingQuestion);
    });
  }
}