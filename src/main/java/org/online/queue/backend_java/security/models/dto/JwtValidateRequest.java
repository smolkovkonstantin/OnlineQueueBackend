package org.online.queue.backend_java.security.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtValidateRequest {
    @JsonProperty("token")
    private String accessToken;
}
