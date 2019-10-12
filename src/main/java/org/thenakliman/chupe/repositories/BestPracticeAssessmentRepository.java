package org.thenakliman.chupe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.BestPracticeAssessment;

@Repository
public interface BestPracticeAssessmentRepository extends JpaRepository<BestPracticeAssessment, Long> {
}
