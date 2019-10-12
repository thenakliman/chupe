package org.thenakliman.chupe.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.BestPractice;

@Repository
public interface BestPracticeRepository extends JpaRepository<BestPractice, Long> {
  List<BestPractice> findByApplicable(boolean applicable);
}
