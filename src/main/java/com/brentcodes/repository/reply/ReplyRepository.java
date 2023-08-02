package com.brentcodes.repository.reply;

import com.brentcodes.entity.reply.Reply;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
@Transactional(readOnly = true, propagation = Propagation.MANDATORY)
public interface ReplyRepository extends R2dbcRepository<Reply, UUID> {
    Flux<Reply> findByConversationIdOrderByCreatedTimeDesc(UUID conversationId);
}
