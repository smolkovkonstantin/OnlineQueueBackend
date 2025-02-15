package org.online.queue.backend_java.services;

import org.online.queue.backend_java.models.Account;
import org.online.queue.backend_java.security.models.dto.CreateAccountDto;


public interface AccountService {

    Account getAccount(Long id);

    Account getAccountWithPositionsAndCredentials(Long id);

    void createAccount(CreateAccountDto createAccountDto);

    Account getAccountWithPositions(Long id);

    Account getAccountWithCredentials();

    Account getAccountWithCredentials(Long accountId);
}
