package com.gfl.client.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("SecurityAccessHandler")
public class SecurityAccessHandler {

    public boolean authHasName(String name) {
        String authName = getAuthentication().getName();
        return authName.equals(name);
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
