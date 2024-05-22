package org.online.queue.backend_java.services;

import org.online.queue.backend_java.models.QueuePositionRequest;
import org.online.queue.backend_java.models.QueueRequest;
import org.online.queue.backend_java.models.QueueResponse;
import org.online.queue.backend_java.models.QueueResponseModel;
import org.online.queue.backend_java.models.QueueUpdateRequest;
import org.online.queue.backend_java.security.models.Credentials;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

public interface QueueService {

    /**
     * @param queueModel входящие характеристики очереди для её создания
     * @return {@link QueueResponse} ответ с параметрами очереди
     */
    QueueResponse create(QueueRequest queueRequest);

    QueueResponse update(QueueUpdateRequest queueUpdateRequest, Long queueId);

    void delete(Long queueId);

    void entryInQueue(Long queueId, Long userId, QueuePositionRequest queuePositionRequest);

    void exitFromQueue(Long queueId, Long userId, QueuePositionRequest queuePositionRequest);

    QueueResponseModel getQueue(Long queueId);
}
