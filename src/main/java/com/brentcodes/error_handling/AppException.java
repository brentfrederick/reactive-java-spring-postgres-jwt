package com.brentcodes.error_handling;

import com.brentcodes.util.RandomGenerator;
import org.springframework.http.HttpStatusCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AppException extends RuntimeException {
    private final UUID id = RandomGenerator.nextId();
    private final HttpStatusCode httpStatus;
    private List<AppError> errors;
    private Map<String, String> data;

    public AppException(HttpStatusCode httpStatus, Throwable cause, String message) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public AppException(HttpStatusCode httpStatus, List<AppError> errors, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.errors = errors;
    }

    public AppException(HttpStatusCode httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public UUID getId() {
        return id;
    }

    public HttpStatusCode getHttpStatus() {
        return httpStatus;
    }

    public void addError(ErrorType type, String attribute) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(new AppError(type, attribute));
    }

    public List<AppError> getErrors() {
        return errors;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}

