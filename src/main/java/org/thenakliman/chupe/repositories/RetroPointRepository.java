package org.thenakliman.chupe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.RetroPoint;

import java.util.List;


@Repository
public interface RetroPointRepository extends JpaRepository<RetroPoint, Long> {
  List<RetroPoint> findAllByRetroId(Long retroId);
}
