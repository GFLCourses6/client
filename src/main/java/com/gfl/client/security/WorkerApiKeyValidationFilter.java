package com.gfl.client.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WorkerApiKeyValidationFilter extends OncePerRequestFilter {

    @Value("${executor.service.auth.token.value}")
    private String workerApiKey;

    private final RsaManager rsaManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String tokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (tokenHeader != null && tokenHeader.startsWith("Token ")) {
            String token = tokenHeader.substring(6);

            String decryptedToken = rsaManager.decrypt(token);

            if (workerApiKey.equals(decryptedToken)) {
                // set Authentication in Security Context with role `WORKER`
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        "worker", null, getWorkerAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    private Collection<? extends GrantedAuthority> getWorkerAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_WORKER"));
    }
}
