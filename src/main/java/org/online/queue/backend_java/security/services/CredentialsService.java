package org.online.queue.backend_java.security.services;

import org.online.queue.backend_java.models.SignUpRequest;
import org.online.queue.backend_java.security.models.Credentials;
import org.online.queue.backend_java.security.models.dto.SignInDto;

public interface CredentialsService {
    Credentials createCredentials(SignUpRequest signUpRequest);

    Credentials getCredentials(SignInDto signInDto);

    Credentials getCredentials(Long userId);

    Credentials getCredentials();
}
