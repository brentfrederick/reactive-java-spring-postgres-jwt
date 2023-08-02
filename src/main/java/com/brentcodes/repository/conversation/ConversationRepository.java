package com.brentcodes.repository.conversation;

import com.brentcodes.entity.conversation.Conversation;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
@Transactional(readOnly = true, propagation = Propagation.MANDATORY)
public interface ConversationRepository extends R2dbcRepository<Conversation, UUID> {

    Flux<Conversation> findAllByAuthorIdOrderByCreatedTimeDesc(UUID authorId);
}
