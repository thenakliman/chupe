package org.thenakliman.chupe.services;

import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.dto.QuestionDTO;
import org.thenakliman.chupe.models.Question;
import org.thenakliman.chupe.repositories.QuestionRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class QuestionService {

  @Autowired
  private QuestionRepository questionsRepository;

  @Autowired
  private ModelMapper modelMapper;

  public QuestionDTO addQuestion(Question question) {
    return modelMapper.map(questionsRepository.save(question), QuestionDTO.class);
  }

  public List<QuestionDTO> getQuestions() {
    return questionsRepository.findAll()
        .stream()
        .map(question -> modelMapper.map(question, QuestionDTO.class))
        .collect(Collectors.toList());
  }

  /**
   * Update a question.
   *
   * @param id       question id
   * @param question question dto containing new question details
   * @throws NotFoundException if question does not exist, we are trying to update
   */
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