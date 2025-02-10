package org.online.queue.backend_java.services.queue.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.online.queue.backend_java.enums.ActionQueue;
import org.online.queue.backend_java.enums.ErrorMessage;
import org.online.queue.backend_java.exception.NotFoundException;
import org.online.queue.backend_java.models.Account;
import org.online.queue.backend_java.models.Position;
import org.online.queue.backend_java.models.Queue;
import org.online.queue.backend_java.models.dto.QueueDto;
import org.online.queue.backend_java.models.dto.QueuePositionDto;
import org.online.queue.backend_java.services.queue.QueueInterface;
import org.online.queue.backend_java.repositories.PositionRepository;
import org.online.queue.backend_java.repositories.QueueRepository;
import org.online.queue.backend_java.services.QueueLogService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("SIMPLE_QUEUE")
@CacheConfig(cacheNames = "queue")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SimpleQueueImpl implements QueueInterface {

    private static final Integer ZERO_INTERVAL = 0;
    private static final Integer ZERO_QUEUE_NUMBER = 0;

    PositionRepository positionRepository;
    QueueRepository queueRepository;
    QueueLogService queueLogService;

    @Override
    @Transactional
    @CachePut(key = "#queueDto.queueId")
    public Queue create(QueueDto queueDto) {
        var queue = new Queue();

        buildQueue(queueDto, queue);

        var ownerId = queueDto
                .getAccount()
                .getId();

        queue.setOwnerId(ownerId);

        queueLogService.save(queue, ActionQueue.CREATE_QUEUE.set(queue.getName()));

        return save(queue);
    }

    @Override
    @CachePut(key = "#queueDto.queueId")
    public Queue update(QueueDto queueDto) {
        var queue = getQueue(queueDto.getQueueId());

        buildQueue(queueDto, queue);

        return save(queue);
    }

    private void buildQueue(QueueDto queueDto, Queue queue) {
        queue.setName(queueDto.getQueueName())
                .setDescription(queueDto.getDescription())
                .setSize(queueDto.getSize())
                .setStartTime(queueDto.getStartTime())
                .setEndTime(queueDto.getEndTime())
                .setOpenTimestamp(queueDto.getOpenTimestamp())
                .setInterval(ZERO_INTERVAL);
    }

    @Override
    @CachePut(key = "#queuePositionDto.queueId")
    public Queue addUser(QueuePositionDto queuePositionDto) {
        var queue = getQueue(queuePositionDto.getQueueId());

        var account = queuePositionDto.getAccount();

        var position = createPosition(queue, account);

        account.getPositions().add(position);
        queue.getPositions().add(position);

        queueLogService.save(queue, ActionQueue.USER_ENTER_TO_QUEUE.set(
                account.getFirstName(),
                queue.getName(),
                position.getQueueNumber()
        ));

        return save(queue);
    }

    private Position createPosition(Queue queue, Account account) {
        var position = positionRepository.findMaxQueueNumberInQueue(queue.getId())
                .orElse(new Position().setQueueNumber(ZERO_QUEUE_NUMBER));

        var currentQueueNumber = position.getQueueNumber();
        position.setQueueNumber(++currentQueueNumber);

        position.setQueue(queue);
        position.setAccount(account);

        return position;
    }

    @Override
    @CachePut(key = "#entryQueueDto.queueId")
    public Queue deleteUser(QueuePositionDto entryQueueDto) {
        throw new UnsupportedOperationException();
    }

    private Queue save(Queue queue) {
        return queueRepository.saveAndFlush(queue);
    }

    private Queue getQueue(Long queueId) {
        return queueRepository.findByIdOrderByPositionsQueueNumber(queueId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.QUEUE_NOT_FOUND.createResponseModel(queueId)));
    }

    @Override
    @Cacheable
    public Queue getCachedQueue(Long id) {
        return queueRepository.findByIdOrderByPositionsQueueNumber(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.QUEUE_NOT_FOUND.createResponseModel(id)));
    }

}
