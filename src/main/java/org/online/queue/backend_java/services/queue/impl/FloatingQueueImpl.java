package org.online.queue.backend_java.services.queue.impl;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.online.queue.backend_java.enums.ErrorMessage;
import org.online.queue.backend_java.exception.ConflictException;
import org.online.queue.backend_java.exception.NotFoundException;
import org.online.queue.backend_java.models.Account;
import org.online.queue.backend_java.models.Position;
import org.online.queue.backend_java.models.Queue;
import org.online.queue.backend_java.models.dto.QueueDto;
import org.online.queue.backend_java.models.dto.QueuePositionDto;
import org.online.queue.backend_java.services.queue.QueueInterface;
import org.online.queue.backend_java.repositories.PositionRepository;
import org.online.queue.backend_java.repositories.QueueRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("FLOATING_QUEUE")
@CacheConfig(cacheNames = "queue")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FloatingQueueImpl implements QueueInterface {

    PositionRepository positionRepository;
    QueueRepository queueRepository;

    @Override
    @Transactional
    @CachePut(key = "#queueDto.queueId")
    public Queue create(QueueDto queueDto) {
        checkInterval(queueDto.getInterval());

        var queue = new Queue();

        this.buildQueue(queueDto, queue);

        var account = queueDto.getAccount();
        var ownerId = account.getId();
        queue.setOwnerId(ownerId);

        return save(queue);
    }

    @Override
    @CachePut(key = "#queueDto.queueId")
    public Queue update(QueueDto queueDto) {
        var interval = queueDto.getInterval();

        checkInterval(interval);

        var id = queueDto.getQueueId();

        var queue = getQueue(id);

        this.buildQueue(queueDto, queue);

        return queue;
    }

    private void buildQueue(QueueDto queueDto, Queue queue) {
        queue.setName(queueDto.getQueueName())
                .setDescription(queueDto.getDescription())
                .setSize(queueDto.getSize())
                .setStartTime(queueDto.getStartTime())
                .setEndTime(queueDto.getEndTime())
                .setInterval(queueDto.getInterval())
                .setOpenTimestamp(queueDto.getOpenTimestamp());
    }

    private void checkInterval(Integer interval) {
        if (interval == null || interval <= 0) {
            throw new ConflictException(ErrorMessage.INTERVAL_EXCEPTION.createResponseModel(interval));
        }
    }

    @Override
    @CachePut(key = "#queuePositionDto.queueId")
    public Queue addUser(QueuePositionDto queuePositionDto) {

        var queueNumber = queuePositionDto.getQueueNumber();

        var queue = getQueue(queuePositionDto.getQueueId());

        checkQueueNumber(queue.getSize(), queueNumber);

        var account = queuePositionDto.getAccount();

        var position = createPosition(queue, account, queueNumber);

        account.getPositions().add(position);
        queue.getPositions().add(position);

        positionRepository.saveAndFlush(position);

        return queue;
    }

    void checkQueueNumber(Integer size, Integer queueNumber) {
        if (size < queueNumber) {
            throw new ConflictException(ErrorMessage.POSITION_OUTSIDE_SIZE_OF_QUEUE.createResponseModel(queueNumber, size));
        }
    }

    private Position createPosition(Queue queue, Account account, Integer queueNumber) {
        var position = getNewPosition(queue, queueNumber);
        position.setQueueNumber(queueNumber);
        position.setAccount(account);
        position.setQueue(queue);
        return position;
    }

    private Position getNewPosition(Queue queue, Integer queueNumber) {

        var opPosition = positionRepository.findByQueueIdAndQueueNumber(queue.getId(), queueNumber);

        if (opPosition.isPresent()) {
            throw new ConflictException(ErrorMessage.POSITION_IS_OCCUPIED.createResponseModel(queueNumber, queue.getName()));
        }
        return new Position();
    }

    @Override
    @CachePut(key = "#queuePositionDto.queueId")
    public Queue deleteUser(QueuePositionDto queuePositionDto) {
        var queueNumber = queuePositionDto.getQueueNumber();

        var queue = getQueue(queuePositionDto.getQueueId());
        var account = queuePositionDto.getAccount();
        var position = getPosition(queue, account.getId(), queueNumber);

        positionRepository.deleteById(position.getId());

        account.getPositions().remove(position);
        queue.getPositions().remove(position);

        return save(queue);
    }

    private Queue getQueue(Long id) {
        return queueRepository.findByIdOrderByPositionsQueueNumber(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.QUEUE_NOT_FOUND.createResponseModel(id)));
    }

    @Override
    @Cacheable
    public Queue getCachedQueue(Long id) {
        return queueRepository.findByIdOrderByPositionsQueueNumber(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.QUEUE_NOT_FOUND.createResponseModel(id)));
    }

    private Queue save(Queue queue) {
        return queueRepository.saveAndFlush(queue);
    }

    private Position getPosition(Queue queue, Long accountId, Integer queueNumber) {
        var opPosition = positionRepository.findByQueueIdAndAccountIdAndQueueNumber(queue.getId(), accountId, queueNumber);

        if (opPosition.isEmpty()) {
            throw new ConflictException(ErrorMessage.POSITION_IS_NOT_EXISTS.createResponseModel(queueNumber, queue.getId()));
        }
        return opPosition.get();
    }
}
