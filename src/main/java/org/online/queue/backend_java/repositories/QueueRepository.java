package org.online.queue.backend_java.repositories;

import org.online.queue.backend_java.models.Queue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QueueRepository extends JpaRepository<Queue, Long> {
    @Query("SELECT q FROM Queue q LEFT JOIN FETCH q.positions WHERE q.id = :queueId")
    Optional<Queue> findByIdWithPositions(Long queueId);

    @Query("SELECT q FROM Queue q LEFT JOIN FETCH q.positions positions WHERE q.id = :queueId order by positions.queueNumber")
    Optional<Queue> findByIdWithPositionsAscByQueueNumber(Long queueId);
}