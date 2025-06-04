package org.online.queue.backend_java.services;

import org.online.queue.backend_java.models.Queue;

public interface QueueLogService {

    void save(Queue queue, String action);
}
