package com.brentcodes.entity.reply;

import com.brentcodes.entity.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class Reply extends BaseEntity {
    @NotNull
    private UUID conversationId;

    @NotBlank
    @Size(max = 2000)
    private String reply;

    public Reply() {
    }

    public Reply(UUID conversationId, String reply) {
        this.conversationId = conversationId;
        this.reply = reply;
    }

    public UUID getConversationId() {
        return conversationId;
    }

    public void setConversationId(UUID conversationId) {
        this.conversationId = conversationId;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
