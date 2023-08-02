package com.brentcodes.api.authentication;

import com.brentcodes.service.authentication.AuthenticationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        Assert.notNull(authenticationService, "authenticationService cannot be null");
        this.authenticationService = authenticationService;
    }

    @PostMapping("/rest/chat/token")
    public Mono<ResponseEntity<AuthenticationResponse>> token(@Valid @RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request.username, request.password, request.client)
                .map(token -> ResponseEntity.ok(new AuthenticationResponse(token)))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }

    record AuthenticationRequest(@NotBlank @Size(max = 255) String username,
                                 @NotBlank @Size(max = 255) String password,
                                 @NotBlank @Size(max = 255) String client) {
    }

    record AuthenticationResponse(@NotBlank String token) {
    }
}
