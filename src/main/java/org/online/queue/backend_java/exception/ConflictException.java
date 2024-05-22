package org.online.queue.backend_java.exception;

import org.online.queue.backend_java.models.ErrorResponse;

import lombok.Getter;

@Getter
public class ConflictException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public ConflictException(ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }
}
