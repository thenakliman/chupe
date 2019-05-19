package org.thenakliman.chupe.repositories.specifications.retroactionitem;

import static java.util.Objects.isNull;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.thenakliman.chupe.models.ActionItem;

public class ActionItemAssignedToSpecification implements Specification<ActionItem> {
  private String assignedTo;

  public ActionItemAssignedToSpecification(String assignedTo) {
    this.assignedTo = assignedTo;
  }

  @Override
  public Predicate toPredicate(Root<ActionItem> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    if (isNull(assignedTo)) {
      return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
    }
    return criteriaBuilder.equal(root.get("assignedTo").get("userName"), this.assignedTo);
  }
}
