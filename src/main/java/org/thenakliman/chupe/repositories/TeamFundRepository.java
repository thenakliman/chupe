package org.thenakliman.chupe.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.Fund;
import org.thenakliman.chupe.models.User;


@Repository
public interface TeamFundRepository extends JpaRepository<Fund, Long> {
  List<Fund> findByOwnerUserName(String user);
}
