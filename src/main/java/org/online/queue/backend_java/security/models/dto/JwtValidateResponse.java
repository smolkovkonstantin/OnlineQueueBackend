package org.online.queue.backend_java.security.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JwtValidateResponse(
        @JsonProperty("userId") Long userId
) {
}
