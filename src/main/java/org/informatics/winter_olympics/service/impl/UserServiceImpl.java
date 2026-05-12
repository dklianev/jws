package org.informatics.winter_olympics.service.impl;

import lombok.RequiredArgsConstructor;
import org.informatics.winter_olympics.data.entity.User;
import org.informatics.winter_olympics.data.repository.UserRepository;
import org.informatics.winter_olympics.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }
}
