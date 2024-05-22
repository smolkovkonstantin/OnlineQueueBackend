package org.online.queue.backend_java.security.repository;

import org.online.queue.backend_java.security.models.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredentialsRepository extends JpaRepository<Credentials, Long> {
    Optional<Credentials> findByEmail(String username);
}