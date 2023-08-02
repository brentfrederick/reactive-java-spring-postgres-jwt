package com.brentcodes.service.user;

import com.brentcodes.entity.user.Users;
import com.brentcodes.error_handling.NotFoundException;
import com.brentcodes.repository.user.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@Validated
public class UserService implements ReactiveUserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Mono<Users> create(@Valid @NotNull Users user) {
        Mono<Users> existingUserMono = userRepository.findByUsername(user.getUsername());

        return existingUserMono
                .flatMap(existingUser -> {
                    existingUser.setPushId(user.getPushId());
                    existingUser.setBuildId(user.getBuildId());
                    existingUser.setCarrier(user.getCarrier());
                    existingUser.setSystemVersion(user.getSystemVersion());
                    existingUser.setAppVersion(user.getAppVersion());
                    existingUser.setAppBuildNumber(user.getAppBuildNumber());
                    return userRepository.save(existingUser);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    return userRepository.save(user);
                }));
    }

    public Mono<Users> findById(@NotNull UUID userId) {
        return userRepository.findById(userId);
    }

    public Mono<Users> getById(@NotNull UUID userId) {
        return userRepository.findById(userId).switchIfEmpty(Mono.error(
                new NotFoundException(MessageFormat.format("User {0} not found", userId))));
    }

    public Mono<Users> getByUsername(@NotBlank String username) {
        return userRepository.findByUsername(username).switchIfEmpty(Mono.error(
                new NotFoundException(MessageFormat.format("Username {0} not found", username))));
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username).cast(UserDetails.class);
    }
}
