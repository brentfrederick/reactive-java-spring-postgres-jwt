package com.brentcodes.error_handling;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record AppError(@NotNull ErrorType type, Optional<String> attribute, Optional<String> msg) {
    public AppError(ErrorType type, String attribute, String msg) {
        this(type, of(attribute), of(msg));
    }

    public AppError(ErrorType type, String attribute) {
        this(type, of(attribute), empty());
    }

    public AppError(ErrorType type) {
        this(type, empty(), empty());
    }
}
