package org.online.queue.backend_java.repositories;

import org.online.queue.backend_java.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.positions WHERE a.id = :accountId")
    Optional<Account> findByIdWithPositions(Long accountId);

    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.positions LEFT JOIN FETCH a.credentials WHERE a.id = :accountId")
    Optional<Account> findByIdWithPositionsAndCredentials(Long accountId);

    @Query("select a from Account a join fetch a.credentials where a.credentials.id = :credentialsId")
    Optional<Account> findByIdWithCredentials(Long credentialsId);
}
