package org.thenakliman.chupe.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.ActionItem;


@Repository
public interface ActionItemRepository extends JpaRepository<ActionItem, Long>, JpaSpecificationExecutor {
  Optional<ActionItem> findByIdAndCreatedByUserNameOrIdAndAssignedToUserName(
      long id1, String createdBy, long id2, String assignedTo);
}
