package org.thenakliman.chupe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.PracticeAssessment;

import java.util.Optional;

@Repository
public interface BestPracticeAssessmentRepository extends JpaRepository<PracticeAssessment, Long> {
  Optional<PracticeAssessment> findByRetroIdAndAnsweredByUserName(Long retroId, String username);
}
