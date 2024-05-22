package org.online.queue.backend_java.factory.product;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.online.queue.backend_java.enums.ErrorMessage;
import org.online.queue.backend_java.exception.ConflictException;
import org.online.queue.backend_java.exception.NotFoundException;
import org.online.queue.backend_java.models.Position;
import org.online.queue.backend_java.models.Queue;
import org.online.queue.backend_java.repositories.PositionRepository;
import org.online.queue.backend_java.repositories.QueueRepository;
import org.online.queue.backend_java.services.AccountService;
import org.online.queue.backend_java.services.QueueLogService;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class QueueAbstract implements QueueInterface {

    QueueRepository queueRepository;
    PositionRepository positionRepository;
    QueueLogService queueLogService;

    protected void saveQueue(Queue queue) {
        queueRepository.saveAndFlush(queue);
    }

    protected void savePosition(Position position) {
        positionRepository.saveAndFlush(position);
    }

    protected void deletePosition(Position position) {
        positionRepository.deleteById(position.getId());
    }

    protected Queue getQueueWithPositions(Long id) {
        return queueRepository.findByIdWithPositions(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.QUEUE_NOT_FOUND.createResponseModel(id)));
    }

    protected Queue getQueue(Long id) {
        return queueRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.QUEUE_NOT_FOUND.createResponseModel(id)));
    }

    protected Position getPosition(Queue queue, Long accountId, Integer queueNumber) {
        var opPosition = positionRepository.findByQueueIdAndAccountIdAndQueueNumber(queue.getId(), accountId, queueNumber);

        if (opPosition.isEmpty()) {
            throw new ConflictException(ErrorMessage.POSITION_IS_NOT_EXISTS.createResponseModel(queueNumber, queue.getId()));
        }
        return opPosition.get();
    }

    protected void logAction(Queue queue, String action) {
        queueLogService.save(queue, action);
    }

}

