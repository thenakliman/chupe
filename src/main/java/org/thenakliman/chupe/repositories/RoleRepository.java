package org.thenakliman.chupe.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  List<Role> findByUsername(String username);
}
