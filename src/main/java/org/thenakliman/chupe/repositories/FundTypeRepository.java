package org.thenakliman.chupe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.FundType;

@Repository
public interface FundTypeRepository extends JpaRepository<FundType, Long> {
}
