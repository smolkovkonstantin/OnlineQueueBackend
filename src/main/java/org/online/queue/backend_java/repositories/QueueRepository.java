package org.online.queue.backend_java.repositories;

import org.online.queue.backend_java.models.Queue;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QueueRepository extends JpaRepository<Queue, Long> {

    @EntityGraph(attributePaths = {"positions", "positions.account"})
    Optional<Queue> findByIdOrderByPositionsQueueNumber(Long id);

}