package org.thenakliman.chupe.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.Answer;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, Long> {
  List<Answer> findByQuestionId(long id);
}