package org.online.queue.backend_java.security.client;

import org.online.queue.backend_java.models.JwtResponse;
import org.online.queue.backend_java.security.models.dto.JwtRequest;
import org.online.queue.backend_java.security.models.dto.JwtUpdateRequest;
import org.online.queue.backend_java.security.models.dto.JwtValidateRequest;
import org.online.queue.backend_java.security.models.dto.JwtValidateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "jwt-client")
public interface JwtClientService {

    @PostMapping(path = "/token")
    ResponseEntity<JwtResponse> createToken(@RequestBody JwtRequest jwtRequest);

    @PostMapping(path = "/validation")
    ResponseEntity<JwtValidateResponse> validateToken(@RequestBody JwtValidateRequest jwtValidateRequest);

    @PutMapping(path = "/token")
    ResponseEntity<JwtResponse> updateToken(@RequestBody JwtUpdateRequest jwtRequest);
}
