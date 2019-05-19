package org.thenakliman.chupe.repositories.specifications.retroactionitem;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.thenakliman.chupe.models.ActionItem;
import org.thenakliman.chupe.models.ActionItemStatus;

public class ActionItemStatusSpecification implements Specification<ActionItem> {
  private List<ActionItemStatus> statuses;

  public ActionItemStatusSpecification(List<ActionItemStatus> statuses) {
    this.statuses = statuses;
  }

  @Override
  public Predicate toPredicate(Root<ActionItem> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    if (this.statuses.isEmpty()) {
      return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
    }
    return root.get("status").in(statuses);
  }
}
