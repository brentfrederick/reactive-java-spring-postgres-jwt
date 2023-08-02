package com.brentcodes.error_handling;

import org.springframework.http.HttpStatus;

public class NotFoundException extends AppException {
    private static final String DEFAULT_MESSAGE = "entity not found";

    public NotFoundException() {
        super(HttpStatus.NOT_FOUND, DEFAULT_MESSAGE);
    }

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
