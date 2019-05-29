package org.thenakliman.chupe.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.ActionItemStatus;
import org.thenakliman.chupe.models.MeetingDiscussionItem;

@Repository
public interface MeetingDiscussionItemRepository extends JpaRepository<MeetingDiscussionItem, Long> {
  List<MeetingDiscussionItem> findByMeetingId(Long id);

  Optional<MeetingDiscussionItem> findByIdAndMeetingId(Long id, Long meetingId);

  List<MeetingDiscussionItem> findByAssignedToUserNameAndStatusIn(
      String userName, List<ActionItemStatus> actionItemStatuses);
}
