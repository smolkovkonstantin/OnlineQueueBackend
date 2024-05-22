package org.online.queue.backend_java.security.services;

import org.online.queue.backend_java.models.JwtResponse;
import org.online.queue.backend_java.models.RefreshRequest;
import org.online.queue.backend_java.models.SignUpRequest;
import org.online.queue.backend_java.security.models.dto.SignInDto;

public interface AuthService {

    void signUp(SignUpRequest signUpRequest);

    JwtResponse signIn(SignInDto signInResponse);

    JwtResponse updateToken(RefreshRequest refreshRequest, String refreshToken);
}
