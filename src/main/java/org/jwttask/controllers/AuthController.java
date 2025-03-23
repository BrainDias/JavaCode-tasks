package org.jwttask.controllers;

import lombok.RequiredArgsConstructor;
import org.jwttask.dtos.AuthRequest;
import org.jwttask.entities.User;
import org.jwttask.enums.Role;
import org.jwttask.repositories.UserRepository;
import org.jwttask.security.jwt.JwtUtils;
import org.jwttask.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {
        User user = userService.register(request.getUsername(), request.getPassword(), Role.USER);
        return ResponseEntity.ok("User registered: " + user.getUsername());
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (!user.isAccountNonLocked()) {
                return ResponseEntity.status(HttpStatus.LOCKED).body("Account is locked");
            }
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                userService.resetFailedAttempts(user);
                String token = jwtUtils.generateToken((UserDetails) authentication.getPrincipal());
                return ResponseEntity.ok(token);
            } catch (BadCredentialsException e) {
                userService.increaseFailedAttempts(user);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
    }

}

