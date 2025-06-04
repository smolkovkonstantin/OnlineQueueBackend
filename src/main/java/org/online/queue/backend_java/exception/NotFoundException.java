package org.online.queue.backend_java.exception;

import lombok.Getter;
import org.online.queue.backend_java.models.ErrorResponse;

@Getter
public class NotFoundException extends RuntimeException {


    private final ErrorResponse errorResponse;

    public NotFoundException(ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }
}
