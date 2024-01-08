package com.gfl.client.controller;

import com.gfl.client.model.ProxyCredentialsRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/proxy")
public class ProxySourceController {

    @PostMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<ProxyCredentialsRequest> addProxy(
            @RequestBody @Valid ProxyCredentialsRequest proxy) {
        // todo: implement logic to add proxy
        return ResponseEntity.ok(proxy);
    }
}
