package org.online.queue.backend_java.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.online.queue.backend_java.security.models.dto.JwtValidateRequest;
import org.online.queue.backend_java.security.services.CredentialsService;
import org.online.queue.backend_java.security.services.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    JwtService jwtService;

    CredentialsService credentialsService;

    static String AUTHORIZATION = "Authorization";
    static String BEARER = "Bearer";
    static int BEGIN_INDEX = 7;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var authHeader = request.getHeader(AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(BEARER)) {

            filterChain.doFilter(request, response);
            return;
        }

        var accessToken = authHeader.substring(BEGIN_INDEX);

        var userId = validateToken(accessToken);

        setAuthentication(request, userId);

        filterChain.doFilter(request, response);
    }

    private Long validateToken(String accessToken) {

        var jwtValidateRequest = new JwtValidateRequest(accessToken);

        var response = jwtService.validateToken(jwtValidateRequest);

         return response.userId();
    }

    private void setAuthentication(HttpServletRequest request, Long userId) {

        var userDetails = credentialsService.getCredentials(userId);

        var authentication = new UsernamePasswordAuthenticationToken(userDetails,
                        null,
                        userDetails.getAuthorities());

        var details = new WebAuthenticationDetailsSource().buildDetails(request);

        authentication.setDetails(details);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
