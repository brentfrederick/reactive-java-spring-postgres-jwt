package com.brentcodes.entity.conversation;

import com.brentcodes.entity.BaseEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class Conversation extends BaseEntity {
    @NotNull
    private UUID authorId;
    @Size(max = 1000)
    private String starter;

    public Conversation() {
    }

    public Conversation(UUID authorId, String starter) {
        this.authorId = authorId;
        this.starter = starter;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public String getStarter() {
        return starter;
    }

    public void setStarter(String starter) {
        this.starter = starter;
    }
}
