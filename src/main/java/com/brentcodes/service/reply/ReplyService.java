package com.brentcodes.service.reply;

import com.brentcodes.entity.conversation.Conversation;
import com.brentcodes.entity.reply.Reply;
import com.brentcodes.entity.user.Users;
import com.brentcodes.repository.reply.ReplyRepository;
import com.brentcodes.service.conversation.ConversationService;
import com.brentcodes.service.user.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@Validated
public class ReplyService {
    private final ReplyRepository replyRepository;

    private final UserService userService;

    private final ConversationService conversationService;

    public ReplyService(ReplyRepository replyRepository, UserService userService, ConversationService conversationService) {
        this.replyRepository = replyRepository;
        this.userService = userService;
        this.conversationService = conversationService;
    }

    @Transactional
    public Mono<Reply> save(@Valid @NotNull Reply reply) {
        return replyRepository.save(reply);
    }

    public Flux<Reply> findAllByConversation(@NotBlank String username, @NotNull UUID conversationId) {
        Mono<Users> userMono = userService.getByUsername(username);
        Mono<Conversation> conversationMono = conversationService.getById(conversationId);

        return userMono.zipWith(conversationMono).flatMapMany(tuple ->
                replyRepository.findByConversationIdOrderByCreatedTimeDesc(tuple.getT2().getId())
        );
    }
}
