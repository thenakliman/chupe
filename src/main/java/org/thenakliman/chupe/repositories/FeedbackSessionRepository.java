package org.thenakliman.chupe.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.FeedbackSession;

@Repository
public interface FeedbackSessionRepository extends JpaRepository<FeedbackSession, Long> {
  List<FeedbackSession> findAllByCreatedByUserName(String username);

  Optional<FeedbackSession> findByIdAndCreatedByUserName(long id, String username);
}
