package org.online.queue.backend_java.repositories;

import org.online.queue.backend_java.models.Queue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueueRepository extends JpaRepository<Queue, Long> {

}