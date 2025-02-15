package org.online.queue.backend_java.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.online.queue.backend_java.enums.ErrorMessage;
import org.online.queue.backend_java.exception.ConflictException;
import org.online.queue.backend_java.exception.NotFoundException;
import org.online.queue.backend_java.models.Account;
import org.online.queue.backend_java.repositories.AccountRepository;
import org.online.queue.backend_java.security.models.dto.CreateAccountDto;
import org.online.queue.backend_java.security.services.CredentialsService;
import org.online.queue.backend_java.services.AccountService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountServiceImpl implements AccountService {

    AccountRepository accountRepository;
    CredentialsService credentialsService;

    @Override
    public Account getAccount(Long id) {

        var account = accountRepository.findById(id);

        if (account.isEmpty()) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND_BY_ID.createResponseModel(id));
        }

        return account.get();
    }

    @Override
    public Account getAccountWithPositions(Long id) {
        return accountRepository.findByIdWithPositions(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND_BY_ID.createResponseModel(id)));
    }

    @Override
    public Account getAccountWithCredentials() {

        var credentials = credentialsService.getCredentials();

        var credentialsId = credentials.getId();

        return accountRepository.findByIdWithCredentials(credentialsId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND_BY_ID.createResponseModel(credentialsId)));
    }

    @Override
    public Account getAccountWithCredentials(Long accountId) {
        var account = getAccountWithCredentials();

        if (!account.getId().equals(accountId)) {
            throw new ConflictException(ErrorMessage.USER_NOT_FOUND_BY_ID.createResponseModel(accountId));
        }

        return account;
    }

    @Override
    public Account getAccountWithPositionsAndCredentials(Long id) {
        var account = accountRepository.findByIdWithPositionsAndCredentials(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND_BY_ID.createResponseModel(id)));

        var credentials = credentialsService.getCredentials();

        if (!credentials.getId().equals(account.getCredentials().getId())) {
            throw new ConflictException(ErrorMessage.PERMISSION_EXCEPTION.createResponseModel());
        }

        return account;
    }

    @Override
    public void createAccount(CreateAccountDto createAccountDto) {

        var account = new Account()
                .setFirstName(createAccountDto.getFirstName())
                .setLastName(createAccountDto.getLastName())
                .setCredentials(createAccountDto.getCredentials());

        accountRepository.saveAndFlush(account);
    }
}
