package org.online.queue.backend_java.factory.product.impl;

import org.online.queue.backend_java.enums.ActionQueue;
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

public class SimpleQueueImpl extends QueueAbstract {

    private static final Integer ZERO_INTERVAL = 0;
    private static final Integer ZERO_QUEUE_NUMBER = 0;

    QueueMapper queueMapper;
    PositionRepository positionRepository;

    public SimpleQueueImpl(QueueRepository queueRepository,
                           PositionRepository positionRepository,
                           QueueLogService queueLogService,
                           QueueMapper queueMapper) {
        super(queueRepository, positionRepository, queueLogService);
        this.queueMapper = queueMapper;
        this.positionRepository = positionRepository;
    }

    @Override
    public QueueResponse create(QueueDto queueDto) {
        var queue = new Queue();

        this.buildQueue(queueDto, queue);

        var account = queueDto.getAccount();
        var owner_id = account.getId();
        queue.setOwnerId(owner_id);

        saveQueue(queue);

        super.logAction(queue, ActionQueue.CREATE_QUEUE.set(queue.getName()));

        return queueMapper.queueToQueueResponse(queue);
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
    @Transactional
    public QueueResponse update(QueueDto queueDto) {
        var queue = getQueue(queueDto.getQueueId());

        this.buildQueue(queueDto, queue);

        return queueMapper.queueToQueueResponse(queue);
    }

    @Override
    @Transactional
    public void addUser(QueuePositionDto queuePositionDto) {
        var queue = getQueueWithPositions(queuePositionDto.getQueueId());

        var account = queuePositionDto.getAccount();

        var position = createPosition(queue, account);

        account.getPositions().add(position);
        queue.getPositions().add(position);

        super.savePosition(position);

        super.logAction(queue, ActionQueue.USER_ENTER_TO_QUEUE.set(
                account.getFirstName(),
                queue.getName(),
                position.getQueueNumber()
        ));
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
    public void deleteUser(QueuePositionDto entryQueueDto) {
        throw new UnsupportedOperationException();
    }
}
