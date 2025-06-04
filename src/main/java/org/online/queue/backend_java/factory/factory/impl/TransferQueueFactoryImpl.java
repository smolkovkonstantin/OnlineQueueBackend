package org.online.queue.backend_java.factory.factory.impl;

import org.online.queue.backend_java.factory.factory.TransformationQueueFactory;
import org.springframework.stereotype.Component;

@Component
public class TransferQueueFactoryImpl implements TransformationQueueFactory {
    @Override
    public void transformSimpleQueueFromFloatingQueue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void transformFloatingQueueFromSimpleQueue() {
        throw new UnsupportedOperationException();
    }
}
