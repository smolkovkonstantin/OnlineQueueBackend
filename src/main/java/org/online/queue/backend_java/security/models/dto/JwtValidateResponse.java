package org.online.queue.backend_java.security.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JwtValidateResponse(
        @JsonProperty("user_id") Long userId
) {
}
