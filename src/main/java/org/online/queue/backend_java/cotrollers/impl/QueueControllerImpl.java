package org.online.queue.backend_java.cotrollers.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.online.queue.backend_java.controllers.api.QueueApi;
import org.online.queue.backend_java.models.QueuePositionRequest;
import org.online.queue.backend_java.models.QueueRequest;
import org.online.queue.backend_java.models.QueueResponse;
import org.online.queue.backend_java.models.QueueResponseModel;
import org.online.queue.backend_java.models.QueueUpdateRequest;
import org.online.queue.backend_java.services.QueueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QueueControllerImpl implements QueueApi {

    QueueService queueService;

    @Override
    public ResponseEntity<QueueResponse> createQueue(QueueRequest queueRequest) {
        var createQueueResponse = queueService.create(queueRequest);
        return new ResponseEntity<>(createQueueResponse, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<QueueResponse> updateQueue(Long queueId, QueueUpdateRequest queueUpdateRequest) {
        var updateQueueResponse = queueService.update(queueUpdateRequest, queueId);
        return new ResponseEntity<>(updateQueueResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteQueue(Long queueId) {
        queueService.delete(queueId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> entryToQueue(Long queueId, Long userId, QueuePositionRequest queuePositionRequest) {

        queueService.entryInQueue(queueId, userId, queuePositionRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Void> exitFromQueue(Long queueId,
                                              Long userId,
                                              QueuePositionRequest queuePositionRequest) {
        queueService.exitFromQueue(queueId, userId, queuePositionRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<QueueResponseModel> getQueue(Long queueId) {
        var queueResponseModel = queueService.getQueue(queueId);

        return new ResponseEntity<>(queueResponseModel, HttpStatus.OK);
    }
}
