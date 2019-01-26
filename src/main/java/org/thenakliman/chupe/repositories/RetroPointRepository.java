package org.thenakliman.chupe.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.Retro;
import org.thenakliman.chupe.models.RetroPoint;

@Repository
public interface RetroPointRepository extends JpaRepository<RetroPoint, Long> {
  public List<RetroPoint> findByRetro(Retro retro);
}
