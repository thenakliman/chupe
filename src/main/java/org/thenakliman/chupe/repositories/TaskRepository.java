package org.thenakliman.chupe.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.Task;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
  Optional<Task> findByIdAndCreatedByUserName(Long id, String username);

  List<Task> findByCreatedByUserName(String username);
}
