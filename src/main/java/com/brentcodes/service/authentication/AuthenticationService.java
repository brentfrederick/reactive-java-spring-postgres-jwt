package com.brentcodes.service.authentication;

import com.brentcodes.service.user.UserService;
import com.brentcodes.util.RSAKeyGenerator;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.time.Instant;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Validated
public class AuthenticationService {
    public static final long JWT_ACCESS_TOKEN_EXPIRY = 86400;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final JwtEncoder jwtEncoder;

    private final JwsHeader jwsHeader;

    private final String issuer;

    public AuthenticationService(UserService userService, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder,
                                 @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuer) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.jwsHeader = JwsHeader
                .with(SignatureAlgorithm.RS256)
                .keyId(RSAKeyGenerator.readRsaKey().getKeyID())
                .build();
        this.issuer = issuer;
    }

    public static Mono<String> getCurrentUsername() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .switchIfEmpty(Mono.error(new IllegalStateException("Missing authentication")))
                .map(Principal::getName);
    }

    public Mono<String> authenticate(@NotEmpty String username, @NotEmpty String password, @NotEmpty String clientId) {
        return userService.findByUsername(username)
                .filter(userDetails -> passwordEncoder.matches(password, userDetails.getPassword()))
                .map(userDetails -> generateToken(userDetails, clientId));
    }

    private String generateToken(UserDetails userDetails, String clientId) {
        Instant now = Instant.now();
        JwtClaimsSet jwtClaims = JwtClaimsSet.builder()
                .issuer(issuer)
                .audience(List.of(clientId))
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(JWT_ACCESS_TOKEN_EXPIRY))
                .notBefore(now)
                .claim(OAuth2ParameterNames.SCOPE, userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, jwtClaims)).getTokenValue();
    }
}
