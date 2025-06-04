package org.online.queue.backend_java.security.services;

import org.online.queue.backend_java.models.JwtResponse;
import org.online.queue.backend_java.security.models.dto.JwtRequest;
import org.online.queue.backend_java.security.models.dto.JwtUpdateRequest;
import org.online.queue.backend_java.security.models.dto.JwtValidateRequest;
import org.online.queue.backend_java.security.models.dto.JwtValidateResponse;

public interface JwtService {
    JwtResponse createToken(JwtRequest jwtCreateRequest);

    JwtResponse updateToken(JwtUpdateRequest jwtupdateRequest);

    JwtValidateResponse validateToken(JwtValidateRequest jwtValidateRequest);
}
