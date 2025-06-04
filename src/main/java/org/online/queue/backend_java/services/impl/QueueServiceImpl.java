package org.online.queue.backend_java.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.online.queue.backend_java.enums.ErrorMessage;
import org.online.queue.backend_java.enums.QueueType;
import org.online.queue.backend_java.exception.ConflictException;
import org.online.queue.backend_java.exception.NotFoundException;
import org.online.queue.backend_java.factory.QueueAbstractFactory;
import org.online.queue.backend_java.mappers.QueueMapper;
import org.online.queue.backend_java.models.QueuePositionRequest;
import org.online.queue.backend_java.models.QueueRequest;
import org.online.queue.backend_java.models.QueueResponse;
import org.online.queue.backend_java.models.QueueResponseModel;
import org.online.queue.backend_java.models.QueueTypeModel;
import org.online.queue.backend_java.models.QueueUpdateRequest;
import org.online.queue.backend_java.models.dto.QueueDto;
import org.online.queue.backend_java.models.dto.QueuePositionDto;
import org.online.queue.backend_java.repositories.QueueRepository;
import org.online.queue.backend_java.services.AccountService;
import org.online.queue.backend_java.services.QueueService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QueueServiceImpl implements QueueService {

    QueueAbstractFactory queueAbstractFactory;
    AccountService accountService;
    QueueRepository queueRepository;
    QueueMapper queueMapper;

    @Override
    public QueueResponse create(QueueRequest queueRequest) {
        var account = accountService.getAccount(queueRequest.getUserId());

        var queueType = getQueueType(queueRequest.getQueueType());

        var queueImpl = queueAbstractFactory.get(queueType);

        var queueDto = QueueDto.builder()
                .account(account)
                .queueName(queueRequest.getName())
                .description(queueRequest.getDescription())
                .size(queueRequest.getSize())
                .interval(queueRequest.getInterval())
                .queueType(queueType)
                .build();
        var createQueueResponse = queueImpl.create(queueDto);

        return createQueueResponse;
    }

    @Override
    public QueueResponse update(QueueUpdateRequest queueUpdateRequest, Long queueId) {
        var queueType = getQueueType(queueUpdateRequest.getQueueType());

        var queueImpl = queueAbstractFactory.get(queueType);

        var queueDto = QueueDto.builder()
                .queueId(queueId)
                .queueName(queueUpdateRequest.getName())
                .description(queueUpdateRequest.getDescription())
                .size(queueUpdateRequest.getSize())
                .interval(queueUpdateRequest.getInterval())
                .queueType(queueType)
                .build();

        var updateQueueResponse = queueImpl.update(queueDto);

        return updateQueueResponse;
    }

    @Override
    public void delete(Long queueId) {

        var account = accountService.getAccountWithCredentials();

        var queue = queueRepository.findById(queueId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.QUEUE_NOT_FOUND.createResponseModel(queueId)));

        if (!account.getId().equals(queue.getOwnerId())) {
            throw new ConflictException(ErrorMessage.ATTEMPT_TO_DELETE_QUEUE_BY_NOT_OWNER.createResponseModel());
        }

        queueRepository.deleteById(queueId);
    }

    @Override
    public void entryInQueue(Long queueId, Long userId, QueuePositionRequest queuePositionRequest) {
        var account = accountService.getAccountWithPositions(userId);
        var queueType = getQueueType(queuePositionRequest.getQueueType());

        var queueImpl = queueAbstractFactory.get(queueType);

        var entryQueueDto = QueuePositionDto.builder()
                .queueId(queueId)
                .account(account)
                .queueNumber(queuePositionRequest.getPosition())
                .build();

        queueImpl.addUser(entryQueueDto);
    }

    @Override
    public void exitFromQueue(Long queueId, Long userId, QueuePositionRequest queuePositionRequest) {
        var account = accountService.getAccountWithPositionsAndCredentials(userId);
        var queueType = getQueueType(queuePositionRequest.getQueueType());

        var queueImpl = queueAbstractFactory.get(queueType);

        var entryQueueDto = QueuePositionDto.builder()
                .queueId(queueId)
                .account(account)
                .queueNumber(queuePositionRequest.getPosition())
                .build();

        queueImpl.deleteUser(entryQueueDto);
    }

    @Override
    public QueueResponseModel getQueue(Long queueId) {
        var queue = queueRepository.findByIdWithPositionsAscByQueueNumber(queueId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.QUEUE_NOT_FOUND.createResponseModel(queueId)));

        return queueMapper.queueToQueueResponseModel(queue);
    }

    private QueueType getQueueType(QueueTypeModel queueTypeModel) {
        return QueueType.valueOf(queueTypeModel.name().toUpperCase());
    }
}
