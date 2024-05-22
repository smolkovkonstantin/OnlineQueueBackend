package org.online.queue.backend_java.security.models.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SignInDto {
    String email;
    String password;
    String deviceId;
}
