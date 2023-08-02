package com.brentcodes.repository;

import com.brentcodes.entity.BaseEntity;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Component
public class DefaultingEntityCallback implements BeforeConvertCallback<BaseEntity> {

    @Override
    public Publisher<BaseEntity> onBeforeConvert(BaseEntity entity, SqlIdentifier table) {
        entity.setCreatedTime(entity.getCreatedTime() != null ? entity.getCreatedTime() : Instant.now());
        entity.setUpdatedTime(Instant.now());
        return Mono.just(entity);
    }
}