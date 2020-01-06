package org.thenakliman.chupe.repositories.specifications.retroactionitem;

import org.springframework.data.jpa.domain.Specification;
import org.thenakliman.chupe.models.RetroActionItem;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static java.util.Objects.isNull;

public class ActionItemRetroSpecification implements Specification<RetroActionItem> {
  private Long retro;

  public ActionItemRetroSpecification(Long retro) {
    this.retro = retro;
  }


  @Override
  public Predicate toPredicate(Root<RetroActionItem> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    if (isNull(retro)) {
      return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
    }
    return criteriaBuilder.equal(root.get("retro"), this.retro);
  }
}
