package org.thenakliman.chupe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.Fund;

import java.util.List;


@Repository
public interface TeamFundRepository extends JpaRepository<Fund, Long> {
  List<Fund> findByOwnerUserName(String user);
}
