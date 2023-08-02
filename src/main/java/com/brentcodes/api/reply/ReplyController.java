package com.brentcodes.api.reply;

import com.brentcodes.entity.conversation.Conversation;
import com.brentcodes.entity.reply.Reply;
import com.brentcodes.entity.user.Users;
import com.brentcodes.error_handling.AppException;
import com.brentcodes.service.authentication.AuthenticationService;
import com.brentcodes.service.conversation.ConversationService;
import com.brentcodes.service.reply.ReplyService;
import com.brentcodes.service.user.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@RestController
@Validated
public class ReplyController {
    private final ReplyService replyService;
    private final UserService userService;
    private final ConversationService conversationService;

    ReplyController(ReplyService replyService, UserService userService, ConversationService conversationService) {
        Assert.notNull(replyService, "replyService cannot be null");
        Assert.notNull(userService, "userService cannot be null");
        Assert.notNull(conversationService, "conversationService cannot be null");
        this.replyService = replyService;
        this.userService = userService;
        this.conversationService = conversationService;
    }

    @PostMapping("/rest/chat/reply/{conversationId}")
    public Mono<ReplyResponse> createReply(@NotNull @PathVariable UUID conversationId,
                                           @Valid @RequestBody CreateReplyRequest request) {
        Mono<Users> user = AuthenticationService.getCurrentUsername().flatMap(userService::getByUsername);
        Mono<Conversation> conversation = conversationService.getById(conversationId);

        return user.zipWith(conversation).flatMap(tuple -> {
            if (!tuple.getT1().getId().equals(tuple.getT2().getAuthorId())) {
                return Mono.error(new AppException(HttpStatus.FORBIDDEN, "no conversation access"));
            } else {
                return replyService.save(new Reply(conversationId, request.reply)).map(ReplyResponse::new);
            }
        });
    }

    record CreateReplyRequest(@NotBlank String reply) {
    }

    @GetMapping("/rest/chat/reply/{conversationId}")
    Flux<ReplyResponse> getReplies(@NotNull @PathVariable UUID conversationId) {
        return AuthenticationService.getCurrentUsername()
                .flatMapMany(username -> replyService.findAllByConversation(username, conversationId))
                .map(ReplyResponse::new);
    }

    public record ReplyResponse(@NotNull UUID id, @NotBlank String reply, @NotNull Instant createdTime) {
        public ReplyResponse(Reply reply) {
            this(reply.getId(), reply.getReply(), reply.getCreatedTime());
        }
    }
}
