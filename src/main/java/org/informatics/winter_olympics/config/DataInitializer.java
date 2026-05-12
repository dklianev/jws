package org.informatics.winter_olympics.config;

import lombok.AllArgsConstructor;
import org.informatics.winter_olympics.data.entity.User;
import org.informatics.winter_olympics.data.entity.Role;
import org.informatics.winter_olympics.data.repository.RoleRepository;
import org.informatics.winter_olympics.data.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        Role admin = createRoleIfMissing("ADMIN");
        Role athlete = createRoleIfMissing("ATHLETE");
        createUserIfMissing("admin1", "admin1", admin);
        createUserIfMissing("athlete1", "athlete1", athlete);
    }

    private Role createRoleIfMissing(String authority) {
        Role role = roleRepository.findByAuthority(authority);
        if (role == null) {
            role = new Role();
            role.setAuthority(authority);
            return roleRepository.save(role);
        }
        return role;
    }

    private void createUserIfMissing(String username, String password, Role role) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setEnabled(true);
            user = userRepository.save(user);
        }
        if (role.getUsers() == null) {
            role.setUsers(new HashSet<>());
        }
        User savedUser = user;
        boolean userHasRole = role.getUsers().stream().anyMatch(item -> item.getId() == savedUser.getId());
        if (!userHasRole) {
            role.getUsers().add(savedUser);
            roleRepository.save(role);
        }
    }
}
