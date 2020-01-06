package org.thenakliman.chupe.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.Answer;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, Long> {
  List<Answer> findByQuestionId(long id);

  Optional<Answer> findByIdAndAnsweredByUserName(long id, String createdBy);
}
