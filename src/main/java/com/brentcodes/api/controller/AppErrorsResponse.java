package com.brentcodes.api.controller;

import com.brentcodes.error_handling.AppError;
import com.brentcodes.error_handling.AppException;
import com.brentcodes.error_handling.ErrorType;
import com.brentcodes.util.RandomGenerator;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
record AppErrorsResponse(@NotNull UUID id, List<AppError> errors, Map<String, String> data) {
    AppErrorsResponse() {
        this(RandomGenerator.nextId(), null, null);
    }

    AppErrorsResponse(AppException appException) {
        this(appException.getId(), appException.getErrors(), appException.getData());
    }

    AppErrorsResponse(ConstraintViolationException cause) {
        this(RandomGenerator.nextId(), cause.getConstraintViolations().stream()
                .map(violation -> new AppError(ErrorType.INVALID_VALUE, violation.getPropertyPath().toString(),
                        violation.getMessage())).toList(), null);
    }

    AppErrorsResponse(MethodArgumentNotValidException cause) {
        this(RandomGenerator.nextId(), cause.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new AppError(ErrorType.INVALID_VALUE, fieldError.getField(),
                        fieldError.getDefaultMessage())).toList(), null);
    }
}
