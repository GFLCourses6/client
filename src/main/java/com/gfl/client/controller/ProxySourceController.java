package com.gfl.client.controller;

import com.gfl.client.model.ProxyCredentialsRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ProxyCredentialsRequest> addProxy(
            @RequestBody @Valid ProxyCredentialsRequest proxy) {
        // todo: implement logic to add proxy
        return ResponseEntity.ok(proxy);
    }
}
