package org.thenakliman.chupe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.BestPractice;

import java.util.List;

@Repository
public interface BestPracticeRepository extends JpaRepository<BestPractice, Long> {
  List<BestPractice> findByApplicable(boolean applicable);
}
