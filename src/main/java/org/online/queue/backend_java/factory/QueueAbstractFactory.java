package org.online.queue.backend_java.factory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.online.queue.backend_java.enums.QueueType;
import org.online.queue.backend_java.factory.factory.CreatorQueueFactory;
import org.online.queue.backend_java.factory.factory.TransformationQueueFactory;
import org.online.queue.backend_java.factory.product.QueueAbstract;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QueueAbstractFactory {

    CreatorQueueFactory queueFactory;
    TransformationQueueFactory transformationQueueFactory;

    public QueueAbstract get(QueueType queueType) {

        return switch (queueType) {
            case FLOATING_QUEUE -> queueFactory.createFloatingQueue();
            case SIMPLE_QUEUE -> queueFactory.createSimpleQueue();
        };
    }
}
