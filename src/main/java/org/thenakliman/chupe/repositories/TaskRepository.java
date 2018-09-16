package org.thenakliman.chupe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.Task;

@Repository
public interface TaskRepository  extends JpaRepository<Task, Long> {
}
