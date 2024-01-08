package com.gfl.client.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/proxy")
public class ProxySourceController {

    @PostMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> addProxy() {
        // todo: implement logic to add proxy
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
