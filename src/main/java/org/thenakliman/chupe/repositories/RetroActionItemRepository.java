package org.thenakliman.chupe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.ActionItemStatus;
import org.thenakliman.chupe.models.RetroActionItem;

import java.util.List;
import java.util.Optional;


@Repository
public interface RetroActionItemRepository extends JpaRepository<RetroActionItem, Long>, JpaSpecificationExecutor {
  Optional<RetroActionItem> findByIdAndCreatedByUserNameOrIdAndAssignedToUserName(
      long id1, String createdBy, long id2, String assignedTo);

  List<RetroActionItem> findByAssignedToUserNameAndStatusIn(String userName, List<ActionItemStatus> actionItemStatuses);
}
