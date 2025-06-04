package org.online.queue.backend_java.factory.factory.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.online.queue.backend_java.factory.factory.CreatorQueueFactory;
import org.online.queue.backend_java.factory.product.QueueAbstract;
import org.online.queue.backend_java.factory.product.impl.FloatingQueueImpl;
import org.online.queue.backend_java.factory.product.impl.SimpleQueueImpl;
import org.online.queue.backend_java.mappers.QueueMapper;
import org.online.queue.backend_java.repositories.PositionRepository;
import org.online.queue.backend_java.repositories.QueueRepository;
import org.online.queue.backend_java.services.AccountService;
import org.online.queue.backend_java.services.QueueLogService;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CreateQueueFactoryImpl implements CreatorQueueFactory {

    QueueRepository queueRepository;
    QueueMapper queueMapper;
    PositionRepository positionRepository;
    AccountService accountService;
    QueueLogService queueLogService;

    @Override
    public QueueAbstract createSimpleQueue() {
        return new SimpleQueueImpl(queueRepository,
                positionRepository,
                queueLogService,
                queueMapper);
    }

    @Override
    public QueueAbstract createFloatingQueue() {
        return new FloatingQueueImpl(queueRepository,
                positionRepository,
                queueMapper,
                queueLogService);
    }
}
