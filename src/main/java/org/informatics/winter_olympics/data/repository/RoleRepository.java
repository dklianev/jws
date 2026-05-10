package org.informatics.winter_olympics.data.repository;

import org.informatics.winter_olympics.data.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByAuthority(String authority);
}
