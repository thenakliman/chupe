package org.thenakliman.chupe.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.Task;
import org.thenakliman.chupe.models.User;


@Repository
public interface TaskRepository  extends JpaRepository<Task, Long> {
  Optional<Task> findById(Long id);

  List<Task> findByCreatedBy(User user);
}
