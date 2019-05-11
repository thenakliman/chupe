package org.thenakliman.chupe.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
  Optional<Question> findByIdAndOwnerUserName(long id, String owner);
}
