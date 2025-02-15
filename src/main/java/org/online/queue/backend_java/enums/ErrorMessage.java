package org.online.queue.backend_java.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.online.queue.backend_java.models.ErrorResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@Getter
public enum ErrorMessage {
    USER_NOT_FOUND_BY_EMAIL("Account not found by email: %s"),
    USER_NOT_FOUND_BY_ID("Account not found by id: %s"),
    QUEUE_NOT_FOUND("Queue not found by id: %s"),
    QUEUE_NUMBER_IS_NULL("Floating queue need position"),
    POSITION_IS_OCCUPIED("The position %s is occupied in queue %s"),
    NUMBER_FORMAT_EXCEPTION("Can't cast to long: %s"),
    INVALID_TOKEN("Authorization failed: %s"),
    UPDATE_TOKEN_FAILED("Update token failed: %s"),
    INTEGRATION_SERVICE_EXCEPTION("Jwt service throw exception: %s"),
    POSITION_IS_ALREADY_OCCUPIED("This place is already occupied: %s. Queue id: %s"),
    POSITION_IS_NOT_EXISTS("This place is not exists: %s. Queue id: %s"),
    POSITION_OUTSIDE_SIZE_OF_QUEUE("Position %s outside the size of the queue with id %s"),
    INTERVAL_EXCEPTION("Interval must be equals or greater zero, but was %s"),
    DUPLICATE_EMAIL("Account with this email already exists"),
    DUPLICATE_USERNAME("Account with this username already exists"),
    PERMISSION_EXCEPTION("You don't have permission to do this"),
    ATTEMPT_TO_DELETE_QUEUE_BY_NOT_OWNER("The attempt to delete the queue is not by the owner");

    private final String errorMessage;

    public ErrorResponse createResponseModel(Object... values) {
        var message = String.format(this.errorMessage, values);
        var dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));

        return new ErrorResponse()
                .message(message)
                .dateTime(dateTime);
    }
}
