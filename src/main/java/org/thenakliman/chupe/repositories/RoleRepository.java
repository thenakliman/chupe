package org.thenakliman.chupe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.Role;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  List<Role> findByUsername(String username);
}
