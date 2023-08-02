package com.brentcodes.entity;

import com.brentcodes.util.RandomGenerator;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;

import java.time.Instant;
import java.util.UUID;

public abstract class BaseEntity implements Persistable<UUID> {
    @Id
    @NotNull
    protected UUID id = RandomGenerator.nextId();

    private Instant createdTime;

    private Instant updatedTime;

    @Override
    public boolean isNew() {
        return createdTime == null;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public Instant getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Instant updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntity that = (BaseEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("createdTime", createdTime)
                .append("updatedTime", updatedTime)
                .toString();
    }
}
