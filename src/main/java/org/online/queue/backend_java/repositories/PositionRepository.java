package org.online.queue.backend_java.repositories;

import org.online.queue.backend_java.models.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {
    Optional<Position> findByQueueIdAndAccountIdAndQueueNumber(Long queueId, Long accountId, Integer queueNumber);

    Optional<Position> findByQueueIdAndQueueNumber(Long id, Integer queueNumber);

    @Query("SELECT max(p.queueNumber) FROM Position p WHERE p.queue.id = :queueId")
    Optional<Position> findMaxQueueNumberInQueue(Long queueId);
}
