package org.online.queue.backend_java.repositories;

import org.online.queue.backend_java.models.QueueLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueueLogRepository extends JpaRepository<QueueLog, Long> {
}