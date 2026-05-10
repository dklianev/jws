package org.informatics.winter_olympics.data.repository;

import org.informatics.winter_olympics.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
