package org.online.queue.backend_java.factory.factory;

import org.online.queue.backend_java.factory.product.QueueAbstract;

public interface CreatorQueueFactory {
    QueueAbstract createSimpleQueue();
    QueueAbstract createFloatingQueue();
}
