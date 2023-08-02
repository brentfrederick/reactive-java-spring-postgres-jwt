package com.brentcodes.api.conversation;

import com.brentcodes.entity.conversation.Conversation;
import com.brentcodes.service.authentication.AuthenticationService;
import com.brentcodes.service.conversation.ConversationService;
import com.brentcodes.service.user.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@RestController
@Validated
class ConversationController {
    private final ConversationService conversationService;
    private final UserService userService;

    ConversationController(UserService userService, ConversationService conversationService) {
        Assert.notNull(conversationService, "conversationService cannot be null");
        Assert.notNull(userService, "userService cannot be null");
        this.conversationService = conversationService;
        this.userService = userService;
    }

    @PostMapping("/rest/chat/conversation")
    Mono<ConversationResponse> createConversation(@Valid @RequestBody CreateConversationRequest request) {
        return AuthenticationService.getCurrentUsername()
                .flatMap(userService::getByUsername)
                .flatMap(user -> conversationService.save(new Conversation(user.getId(), request.starter)))
                .map(ConversationResponse::new);
    }

    record CreateConversationRequest(@NotEmpty @Size(max = 1000) String starter) {
    }

    @GetMapping("/rest/chat/conversation")
    Flux<ConversationResponse> getConversations() {
        return AuthenticationService.getCurrentUsername()
                .flatMap(userService::getByUsername)
                .flatMapMany(user -> conversationService.findAllByAuthor(user)
                        .map(ConversationResponse::new));
    }

    record ConversationResponse(@NotNull UUID id, @NotNull Instant timestamp, @NotBlank String starter) {
        public ConversationResponse(Conversation conversation) {
            this(conversation.getId(), conversation.getCreatedTime(), conversation.getStarter());
        }
    }
}
