package org.thenakliman.chupe.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.Question;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Long> {
}
