package org.online.queue.backend_java.security.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.online.queue.backend_java.enums.ErrorMessage;
import org.online.queue.backend_java.exception.ConflictException;
import org.online.queue.backend_java.exception.NotFoundException;
import org.online.queue.backend_java.models.SignUpRequest;
import org.online.queue.backend_java.security.models.Credentials;
import org.online.queue.backend_java.security.models.dto.CreateAccountDto;
import org.online.queue.backend_java.security.models.dto.SignInDto;
import org.online.queue.backend_java.security.repository.CredentialsRepository;
import org.online.queue.backend_java.security.services.CredentialsService;
import org.online.queue.backend_java.services.AccountService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CredentialsServiceImpl implements CredentialsService {

    PasswordEncoder passwordEncoder;
    CredentialsRepository credentialsRepository;
    AuthenticationManager authenticationManager;

    @Override
    public Credentials createCredentials(SignUpRequest signUpRequest) {
        checkDuplicateEmail(signUpRequest.getEmail());
        var credentials = new Credentials();

        credentials.setEmail(signUpRequest.getEmail());
        credentials.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        credentials.setRole("TEST");

        return credentialsRepository.save(credentials);
    }

    void checkDuplicateEmail(String email) {
        credentialsRepository.findByEmail(email).ifPresent((credentials) -> {
            throw new ConflictException(ErrorMessage.DUPLICATE_EMAIL.createResponseModel());
        });
    }

    @Override
    public Credentials getCredentials(SignInDto signInDto) {
        var authToken = new UsernamePasswordAuthenticationToken(
                signInDto.getEmail(),
                signInDto.getPassword()
        );

        var authentication = authenticationManager
                .authenticate(authToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return (Credentials) authentication.getPrincipal();
    }

    @Override
    public Credentials getCredentials(Long userId) {
        return credentialsRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(ErrorMessage.USER_NOT_FOUND_BY_ID.createResponseModel(userId)));
    }

    @Override
    public Credentials getCredentials() {
        return (Credentials) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
