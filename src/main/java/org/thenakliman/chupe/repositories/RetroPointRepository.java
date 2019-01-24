package org.thenakliman.chupe.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.RetroPoint;

@Repository
public interface RetroPointRepository extends CrudRepository<RetroPoint, Long> {
}
