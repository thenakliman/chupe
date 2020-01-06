package org.thenakliman.chupe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thenakliman.chupe.models.Meeting;

import java.util.List;
import java.util.Optional;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
  List<Meeting> findByCreatedByUserName(String username);

  Optional<Meeting> findByIdAndCreatedByUserName(Long id, String username);
}
