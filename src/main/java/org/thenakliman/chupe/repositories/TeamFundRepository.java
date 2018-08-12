package org.thenakliman.chupe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.Fund;

@Repository
public interface TeamFundRepository extends JpaRepository<Fund, Long> {
}
