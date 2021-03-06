package org.thenakliman.chupe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.FeedbackPoint;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackPointRepository extends JpaRepository<FeedbackPoint, Long> {
  List<FeedbackPoint> findByGivenToUserNameAndFeedbackSessionId(String givenTo, long id);

  Optional<FeedbackPoint> findByGivenByUserNameAndId(String givenTo, long id);

  List<FeedbackPoint> findByGivenToUserNameAndGivenByUserNameAndFeedbackSessionId(
      String givenTo,
      String givenBy,
      long id);
}
