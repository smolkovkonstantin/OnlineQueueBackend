package org.online.queue.backend_java.security.models.dto;

import lombok.Builder;
import lombok.Value;
import org.online.queue.backend_java.security.models.Credentials;

@Value
@Builder
public class CreateAccountDto {
    String firstName;
    String lastName;
    String username;
    Credentials credentials;
}
