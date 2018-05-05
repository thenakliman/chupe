/* TODO(thenakliman) Find a way to test repository integration
package org.thenakliman.chupe.repositories;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.CrudRepository;

@RunWith(MockitoJUnitRunner.class)
public class QuestionRepositoryTest {

@Mock
private CrudRepository crudRepository;

@InjectMocks
private QuestionRepository questionsRepository;
@Test
public void shouldCallSaveMethodOfCrudRepository() {
  Question question = new Question();
  question.setQuestion("What is your name?");
  question.setAssignedTo("testUser1");
  question.setDescription("Need your name for auth service");
  question.setOwner("testUser2");
  BDDMockito.given(crudRepository.save(question)).willReturn(question);
  Question question1 = questionsRepository.save(question);
  assertEquals(question, question1);

}
}
*/