package org.online.queue.backend_java.security.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JwtUpdateRequest(
        @JsonProperty("token") String refreshToken
) {
}
