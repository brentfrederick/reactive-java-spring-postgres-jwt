package com.brentcodes.service.conversation;

import com.brentcodes.entity.conversation.Conversation;
import com.brentcodes.entity.user.Users;
import com.brentcodes.error_handling.NotFoundException;
import com.brentcodes.repository.conversation.ConversationRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@Validated
public class ConversationService {
    private final ConversationRepository conversationRepository;

    public ConversationService(ConversationRepository conversationRepository) {
        Assert.notNull(conversationRepository, "conversationRepository cannot be null");
        this.conversationRepository = conversationRepository;
    }

    @Transactional
    public Mono<Conversation> save(@Valid @NotNull Conversation conversation) {
        return conversationRepository.save(conversation);
    }

    public Flux<Conversation> findAllByAuthor(@NotNull Users user) {
        return conversationRepository.findAllByAuthorIdOrderByCreatedTimeDesc(user.getId());
    }

    public Mono<Conversation> getById(@NotNull UUID id) {
        return conversationRepository.findById(id).switchIfEmpty(Mono.error(
                new NotFoundException(MessageFormat.format("Conversation {0} not found", id))));
    }
}
