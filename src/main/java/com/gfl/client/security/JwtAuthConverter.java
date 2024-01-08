package com.gfl.client.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Value("${keycloak.username.attribute}")
    private String usernameAttribute;

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt token) {
        var authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(token).stream(),
                extractResourceRoles(token).stream()
        ).collect(Collectors.toSet());

        return new JwtAuthenticationToken(token, authorities, getPrincipalName(token));
    }

    private String getPrincipalName(Jwt token) {
        return token.getClaim(usernameAttribute);
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt token) {
        Map<String, Object> resourceAccess;
        Collection<String> roles;

        resourceAccess = token.getClaim("realm_access");
        if (resourceAccess != null) {
            roles = (Collection<String>) resourceAccess.get("roles");
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .toList();
        }
        return Set.of();
    }
}
