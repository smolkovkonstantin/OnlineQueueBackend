package org.online.queue.backend_java.security.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JwtValidateRequest(
        @JsonProperty("token") String accessToken
) {
}
