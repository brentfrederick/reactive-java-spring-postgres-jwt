package com.brentcodes.security;

import com.brentcodes.util.RSAKeyGenerator;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import java.util.ArrayList;
import java.util.List;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration
public class WebSecurityConfig {

    @Bean
    SecurityWebFilterChain defaultSecurityFilterChain(ServerHttpSecurity http, JwtReactiveAuthenticationManager jwtReactiveAuthenticationManager) {
        http
                .authorizeExchange(authorize -> {
                            authorize
                                    .pathMatchers("/rest/chat/user", "/rest/chat/token").permitAll()
                                    .pathMatchers("/rest/chat/**").hasAuthority("SCOPE_USER");
                            authorize.anyExchange().denyAll();
                        }
                )
                .csrf().disable()
                .cors().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .authenticationManager(jwtReactiveAuthenticationManager)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .requestCache().disable()
                .oauth2ResourceServer()
                .jwt();

        return http.build();
    }

    @Bean
    public JwtReactiveAuthenticationManager jwtReactiveAuthenticationManager(ReactiveJwtDecoder reactiveJwtDecoder) {
        return new JwtReactiveAuthenticationManager(reactiveJwtDecoder);
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWKSet jwkSet = new JWKSet(RSAKeyGenerator.readRsaKey());
        return new NimbusJwtEncoder((jwkSelector, securityContext) -> jwkSelector.select(jwkSet));
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder(@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuer) {
        try {
            NimbusReactiveJwtDecoder decoder = NimbusReactiveJwtDecoder
                    .withPublicKey(RSAKeyGenerator.readRsaKey().toRSAPublicKey()).build();

            List<OAuth2TokenValidator<Jwt>> validators = new ArrayList<>();
            validators.add(new JwtTimestampValidator());
            validators.add(new JwtIssuerValidator(issuer));
            decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(validators));

            return decoder;
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }
}
