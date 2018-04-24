package org.thenakliman.chupe.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.Models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
