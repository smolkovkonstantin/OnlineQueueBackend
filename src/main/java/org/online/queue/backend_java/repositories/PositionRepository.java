package org.online.queue.backend_java.repositories;

import org.online.queue.backend_java.models.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {
    Optional<Position> findByQueueIdAndAccountIdAndQueueNumber(Long queueId, Long accountId, Integer queueNumber);

    Optional<Position> findByQueueIdAndQueueNumber(Long id, Integer queueNumber);

    @Query("SELECT max(p.queueNumber) FROM Position p WHERE p.queue.id = :queueId")
    Optional<Position> findMaxQueueNumberInQueue(Long queueId);

    @Query("SELECT p from Position  p where p.queue.id = :queueId and p.queueNumber >= :queueNumber")
    List<Position> findAllPositionsEqualsAndHigherInQueue(@Param("queueId") Long queueId,
                                                          @Param("queueNumber") Integer queueNumber);
}
