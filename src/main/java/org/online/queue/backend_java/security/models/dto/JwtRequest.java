package org.online.queue.backend_java.security.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JwtRequest(
        @JsonProperty("userId") Long userId,
        @JsonProperty("deviceId") String deviceId
) {
}
