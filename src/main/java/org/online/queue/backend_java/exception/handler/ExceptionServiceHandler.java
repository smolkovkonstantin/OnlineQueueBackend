package org.online.queue.backend_java.exception.handler;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.online.queue.backend_java.enums.ErrorMessage;
import org.online.queue.backend_java.exception.ConflictException;
import org.online.queue.backend_java.exception.ForbiddenException;
import org.online.queue.backend_java.exception.NotFoundException;
import org.online.queue.backend_java.models.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionServiceHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception) {
        log.error(exception.getMessage());
        return createResponse(exception.getErrorResponse(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException exception) {
        log.error(exception.getMessage());
        return createResponse(exception.getErrorResponse(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException exception) {
        log.error(exception.getMessage());
        return createResponse(exception.getErrorResponse(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException exception) {

        var errorResponse = ErrorMessage.INTEGRATION_SERVICE_EXCEPTION.createResponseModel(exception.getMessage());

        log.error(errorResponse.getMessage());
        return createResponse(errorResponse, HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<ErrorResponse> createResponse(ErrorResponse errorResponse,
            HttpStatus status) {
        return new ResponseEntity<>(errorResponse, status);
    }
}
