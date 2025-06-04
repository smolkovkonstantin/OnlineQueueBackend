package org.online.queue.backend_java.security.services.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.online.queue.backend_java.models.JwtResponse;
import org.online.queue.backend_java.models.RefreshRequest;
import org.online.queue.backend_java.models.SignUpRequest;
import org.online.queue.backend_java.security.models.Credentials;
import org.online.queue.backend_java.security.models.dto.CreateAccountDto;
import org.online.queue.backend_java.security.models.dto.JwtRequest;
import org.online.queue.backend_java.security.models.dto.JwtUpdateRequest;
import org.online.queue.backend_java.security.models.dto.SignInDto;
import org.online.queue.backend_java.security.services.AuthService;
import org.online.queue.backend_java.security.services.CredentialsService;
import org.online.queue.backend_java.security.services.JwtService;
import org.online.queue.backend_java.services.AccountService;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    CredentialsService credentialsService;
    AccountService accountService;
    JwtService jwtService;

    @Override
    @Transactional
    public void signUp(SignUpRequest signUpRequest) {

        var credentials=  credentialsService.createCredentials(signUpRequest);

        createAccount(signUpRequest, credentials);
    }

    private void createAccount(SignUpRequest signUpRequest, Credentials credentials) {
        var createAccountDto = CreateAccountDto.builder()
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .username(signUpRequest.getUsername())
                .credentials(credentials)
                .build();

        accountService.createAccount(createAccountDto);
    }

    @Override
    public JwtResponse signIn(SignInDto signInDto) {

        var credentials = credentialsService.getCredentials(signInDto);

        var jwtCreateRequest = new JwtRequest(credentials.getId(), signInDto.getDeviceId());

        return jwtService.createToken(jwtCreateRequest);
    }

    @Override
    public JwtResponse updateToken(RefreshRequest refreshRequest, String refreshToken) {
        var jwtupdateRequest = new JwtUpdateRequest(refreshToken);

        return jwtService.updateToken(jwtupdateRequest);
    }
}
