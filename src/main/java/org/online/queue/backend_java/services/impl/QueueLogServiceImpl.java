package org.online.queue.backend_java.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.online.queue.backend_java.models.Queue;
import org.online.queue.backend_java.models.QueueLog;
import org.online.queue.backend_java.repositories.QueueLogRepository;
import org.online.queue.backend_java.services.QueueLogService;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QueueLogServiceImpl implements QueueLogService {

    QueueLogRepository queueLogRepository;

    @Override
    public void save(Queue queue, String action) {
        var queueLog = new QueueLog();

        queueLog.setActionTime(ZonedDateTime.now());
        queueLog.setQueue(queue);
        queueLog.setAction(action);

        queueLogRepository.save(queueLog);
    }
}
