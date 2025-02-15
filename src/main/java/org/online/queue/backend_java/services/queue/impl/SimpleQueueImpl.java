package org.online.queue.backend_java.services.queue.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.online.queue.backend_java.enums.ActionQueue;
import org.online.queue.backend_java.enums.ErrorMessage;
import org.online.queue.backend_java.exception.ConflictException;
import org.online.queue.backend_java.exception.NotFoundException;
import org.online.queue.backend_java.models.Account;
import org.online.queue.backend_java.models.Position;
import org.online.queue.backend_java.models.Queue;
import org.online.queue.backend_java.models.dto.QueueDto;
import org.online.queue.backend_java.models.dto.QueuePositionDto;
import org.online.queue.backend_java.repositories.PositionRepository;
import org.online.queue.backend_java.repositories.QueueRepository;
import org.online.queue.backend_java.services.QueueLogService;
import org.online.queue.backend_java.services.queue.QueueInterface;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component("SIMPLE_QUEUE")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SimpleQueueImpl implements QueueInterface {

    private static final Integer ZERO_INTERVAL = 0;
    private static final Integer ZERO_QUEUE_NUMBER = 0;

    PositionRepository positionRepository;
    QueueRepository queueRepository;
    QueueLogService queueLogService;

    private final static Integer DEFAULT_SIZE = 1000;

    @Override
    @Transactional
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
    public Queue update(QueueDto queueDto) {
        var queue = getQueue(queueDto.getQueueId());

        buildQueue(queueDto, queue);

        return queue;
    }

    private void buildQueue(QueueDto queueDto, Queue queue) {
        queue.setName(queueDto.getQueueName())
                .setDescription(queueDto.getDescription())
                .setSize(Optional.ofNullable(queueDto.getSize()).orElse(DEFAULT_SIZE))
                .setStartTime(queueDto.getStartTime())
                .setEndTime(queueDto.getEndTime())
                .setOpenTimestamp(queueDto.getOpenTimestamp())
                .setInterval(ZERO_INTERVAL);
    }

    @Override
    public void addUser(QueuePositionDto queuePositionDto) {
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

        positionRepository.saveAndFlush(position);
    }

    private Position createPosition(Queue queue, Account account) {
        var position = positionRepository.findMaxQueueNumberInQueue(queue.getId())
                .orElse(new Position(ZERO_QUEUE_NUMBER));

        var currentQueueNumber = position.getQueueNumber();
        position.setQueueNumber(++currentQueueNumber);

        position.setQueue(queue);
        position.setAccount(account);

        return position;
    }

    @Override
    public void deleteUser(QueuePositionDto queuePositionDto) {
        var queueNumber = queuePositionDto.getQueueNumber();

        var account = queuePositionDto.getAccount();
        var queue = getQueue(queuePositionDto.getQueueId());

        var positions = getPositions(queue.getId(), queueNumber);

        var positionToDeleteIndex = findPositionToDeleteIndex(queueNumber, positions);

        positionRepository.delete(positions.get(positionToDeleteIndex));

        account.getPositions().remove(positions.get(positionToDeleteIndex));
        queue.getPositions().remove(positions.get(positionToDeleteIndex));

        positions.remove(positionToDeleteIndex);

        updateHigherPositions(positions);
    }

    private List<Position> getPositions(Long queueId, Integer queueNumber) {
        var positions = positionRepository.findAllPositionsEqualsAndHigherInQueue(queueId, queueNumber);

        if (positions.isEmpty()) {
            throw new ConflictException(ErrorMessage.POSITION_IS_NOT_EXISTS.createResponseModel(queueNumber, queueId));
        }

        return positions;
    }

    private void updateHigherPositions(List<Position> positions) {
        positions.forEach(position -> position.setQueueNumber(position.getQueueNumber() - 1));
    }

    private int findPositionToDeleteIndex(Integer queueNumber, List<Position> positions) {
        for (var i = 0; i < positions.size(); i++) {
            if (positions.get(i).getQueueNumber().equals(queueNumber)) {
                return i;
            }
        }
        throw new RuntimeException();
    }

    private Queue save(Queue queue) {
        return queueRepository.saveAndFlush(queue);
    }

    private Queue getQueue(Long queueId) {
        return queueRepository.findByIdOrderByPositionsQueueNumber(queueId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.QUEUE_NOT_FOUND.createResponseModel(queueId)));
    }

}
