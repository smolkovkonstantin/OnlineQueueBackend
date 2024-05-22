package org.online.queue.backend_java.models.dto;

import lombok.Builder;
import lombok.Value;
import org.online.queue.backend_java.models.Account;

@Value
@Builder
public class InsertUserDto {
    Object position;
    Account user;
}
