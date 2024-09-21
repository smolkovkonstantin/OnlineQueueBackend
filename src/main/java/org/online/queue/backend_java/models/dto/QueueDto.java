package org.online.queue.backend_java.models.dto;

import lombok.Builder;
import lombok.Value;
import org.online.queue.backend_java.models.Account;

import java.time.ZonedDateTime;

@Value
@Builder
public class QueueDto {
    Long queueId;
    Account account;
    String queueName;
    String description;
    Integer size;
    Integer interval;
    ZonedDateTime startTime;
    ZonedDateTime endTime;
    ZonedDateTime openTimestamp;
}
