package org.online.queue.backend_java.factory.product;

import org.online.queue.backend_java.models.QueueResponse;
import org.online.queue.backend_java.models.dto.QueueDto;
import org.online.queue.backend_java.models.dto.QueuePositionDto;

public interface QueueInterface {
    /**
     * Create queue with all settings
     * @param queueDto dto for create queue
     * @return {@link QueueResponse} - response with all queue settings
     */
    QueueResponse create(QueueDto queueDto);

    /**
     * Update queue by request settings
     * @param queueDto dto for update queue
     * @return {@link QueueResponse} - response with all queue settings
     */
    QueueResponse update(QueueDto queueDto);

    /**
     * Create link between account and position, queue and position<br>
     * Add account to queue
     * @param queuePositionDto
     */
    void addUser(QueuePositionDto queuePositionDto);

    /**
     * Break the link between account and position, queue and position <br>
     * Remove account from queue
     * @param entryQueueDto
     */
    void deleteUser(QueuePositionDto entryQueueDto);
}
