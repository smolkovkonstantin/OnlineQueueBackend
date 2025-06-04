package org.online.queue.backend_java.security.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.online.queue.backend_java.enums.ErrorMessage;
import org.online.queue.backend_java.exception.ForbiddenException;
import org.online.queue.backend_java.models.JwtResponse;
import org.online.queue.backend_java.security.client.JwtClientService;
import org.online.queue.backend_java.security.models.dto.JwtRequest;
import org.online.queue.backend_java.security.models.dto.JwtUpdateRequest;
import org.online.queue.backend_java.security.models.dto.JwtValidateRequest;
import org.online.queue.backend_java.security.models.dto.JwtValidateResponse;
import org.online.queue.backend_java.security.services.JwtService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtServiceImpl implements JwtService {

    JwtClientService jwtClientService;

    @Override
    public JwtResponse createToken(JwtRequest jwtCreateRequest) {

        var jwtResponse = jwtClientService.createToken(jwtCreateRequest);

        return jwtResponse.getBody();
    }

    @Override
    public JwtResponse updateToken(JwtUpdateRequest jwtupdateRequest) {

        var jwtResponse = jwtClientService.updateToken(jwtupdateRequest);

        if (jwtResponse.getStatusCode().is4xxClientError()) {
            throw new ForbiddenException(ErrorMessage.UPDATE_TOKEN_FAILED.createResponseModel(jwtResponse.getBody()));
        }

        return jwtResponse.getBody();
    }

    @Override
    public JwtValidateResponse validateToken(JwtValidateRequest jwtValidateRequest) {
        var jwtValidateResponse = jwtClientService.validateToken(jwtValidateRequest);

        if (jwtValidateResponse.getStatusCode().is4xxClientError()) {
            throw new ForbiddenException(ErrorMessage.INVALID_TOKEN.createResponseModel(jwtValidateResponse.getBody()));
        }

        return jwtValidateResponse.getBody();
    }
}
