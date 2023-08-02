package com.brentcodes.repository.user;

import com.brentcodes.entity.user.Users;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@Transactional(readOnly = true, propagation = Propagation.MANDATORY)
public interface UserRepository extends R2dbcRepository<Users, UUID> {

    Mono<Users> findByUsername(String username);
}
