package org.thenakliman.chupe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.Retro;

@Repository
public interface RetroRepository extends JpaRepository<Retro, Long> {
}
