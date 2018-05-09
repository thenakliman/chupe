package org.thenakliman.chupe.transformer;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import org.thenakliman.chupe.dto.QuestionDTO;
import org.thenakliman.chupe.models.Question;

@RunWith(MockitoJUnitRunner.class)
public class QuestionTransformerTest {
  @InjectMocks
  private QuestionTransformer questionTransformer;

  @Test
  public void shouldReturnEmptyArrayIfNullIsReceived() {
    assertEquals(new ArrayList<QuestionDTO>(),
                 questionTransformer.transformToQuestionDTO((List)null));
  }

  @Test
  public void shouldReturnArrayOfQuestions() {
    String questionString = "What is your name?";
    String assignedUser = "testUser1";
    String description = "Need your name for auth service";
    String owner = "testUser2";
    int id = 100;
    Question question1 = new Question();
    question1.setQuestion(questionString);
    question1.setAssignedTo(assignedUser);
    question1.setDescription(description);
    question1.setOwner(owner);
    question1.setId(id);
    List<Question> questions = new ArrayList<>();
    questions.add(question1);

    QuestionDTO questionDTO = new QuestionDTO();
    questionDTO.setId(id);
    questionDTO.setQuestion(questionString);
    questionDTO.setAssignedTo(assignedUser);
    questionDTO.setOwner(owner);
    questionDTO.setDescription(description);
    List<QuestionDTO> questionDTOs = new ArrayList<>();
    questionDTOs.add(questionDTO);

    assertThat(questionDTOs,
               samePropertyValuesAs(questionTransformer.transformToQuestionDTO(questions)));
  }
}