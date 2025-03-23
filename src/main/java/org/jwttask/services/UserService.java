package org.jwttask.services;

import lombok.RequiredArgsConstructor;
import org.jwttask.entities.User;
import org.jwttask.enums.Role;
import org.jwttask.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(String username, String password, Role role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return userRepository.save(user);
    }

    public void increaseFailedAttempts(User user) {
        int newAttempts = user.getFailedLoginAttempts() + 1;
        user.setFailedLoginAttempts(newAttempts);
        if (newAttempts >= 5) {
            user.setAccountNonLocked(false);
        }
        userRepository.save(user);
    }

    public void resetFailedAttempts(User user) {
        user.setFailedLoginAttempts(0);
        user.setAccountNonLocked(true);
        userRepository.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

