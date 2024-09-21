package org.online.queue.backend_java.repositories;

import org.online.queue.backend_java.models.Queue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QueueRepository extends JpaRepository<Queue, Long> {

    Optional<Queue> findByIdOrderByPositionsQueueNumber(Long queueId);
}