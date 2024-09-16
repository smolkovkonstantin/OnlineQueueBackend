package org.online.queue.backend_java.factory.product.impl;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.online.queue.backend_java.enums.ActionQueue;
import org.online.queue.backend_java.enums.ErrorMessage;
import org.online.queue.backend_java.exception.ConflictException;
import org.online.queue.backend_java.factory.product.QueueAbstract;
import org.online.queue.backend_java.mappers.QueueMapper;
import org.online.queue.backend_java.models.Account;
import org.online.queue.backend_java.models.Position;
import org.online.queue.backend_java.models.Queue;
import org.online.queue.backend_java.models.QueueResponse;
import org.online.queue.backend_java.models.dto.QueueDto;
import org.online.queue.backend_java.models.dto.QueuePositionDto;
import org.online.queue.backend_java.repositories.PositionRepository;
import org.online.queue.backend_java.repositories.QueueRepository;
import org.online.queue.backend_java.services.QueueLogService;
import org.springframework.transaction.annotation.Transactional;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FloatingQueueImpl extends QueueAbstract {

    QueueMapper queueMapper;
    PositionRepository positionRepository;

    public FloatingQueueImpl(QueueRepository queueRepository,
                             PositionRepository positionRepository,
                             QueueMapper queueMapper, QueueLogService queueLogService) {
        super(queueRepository, positionRepository, queueLogService);
        this.queueMapper = queueMapper;
        this.positionRepository = positionRepository;
    }

    @Override
    public QueueResponse create(QueueDto queueDto) {
        checkInterval(queueDto.getInterval());

        var queue = new Queue();

        this.buildQueue(queueDto, queue);

        var account = queueDto.getAccount();
        var ownerId = account.getId();
        queue.setOwnerId(ownerId);

        saveQueue(queue);

        super.logAction(queue, ActionQueue.CREATE_QUEUE.set(queue.getName()));

        return queueMapper.queueToQueueResponse(queue);
    }

    @Override
    @Transactional
    public QueueResponse update(QueueDto queueDto) {
        var interval = queueDto.getInterval();

        checkInterval(interval);

        var queue = getQueue(queueDto.getQueueId());

        this.buildQueue(queueDto, queue);

        return queueMapper.queueToQueueResponse(queue);
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
    @Transactional
    public void addUser(QueuePositionDto queuePositionDto) {

        var queueNumber = queuePositionDto.getQueueNumber();

        var queue = getQueueWithPositions(queuePositionDto.getQueueId());

        checkQueueNumber(queue.getSize(), queueNumber);

        var account = queuePositionDto.getAccount();

        var position = createPosition(queue, account, queueNumber);

        account.getPositions().add(position);
        queue.getPositions().add(position);

        super.savePosition(position);

        super.logAction(queue, ActionQueue.USER_ENTER_TO_QUEUE.set(
                account.getFirstName(),
                queue.getName(),
                queueNumber
        ));
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
    @Transactional
    public void deleteUser(QueuePositionDto queuePositionDto) {
        var queueNumber = queuePositionDto.getQueueNumber();

        var queue = getQueueWithPositions(queuePositionDto.getQueueId());
        var account = queuePositionDto.getAccount();
        var position = getPosition(queue, account.getId(), queueNumber);

        super.deletePosition(position);

        account.getPositions().remove(position);
        queue.getPositions().remove(position);
    }
}
