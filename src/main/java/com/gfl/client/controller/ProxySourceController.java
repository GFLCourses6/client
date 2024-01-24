package com.gfl.client.controller;

import com.gfl.client.model.ProxyConfigHolder;
import com.gfl.client.service.proxy.queue.ProxySourceQueueHandler;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/proxy")
public class ProxySourceController implements ProxyController {

    private final ProxySourceQueueHandler proxySourceQueueHandler;
    private final Logger logger = LoggerFactory.getLogger(ProxySourceController.class);

    @PostMapping
    public ResponseEntity<Void> addProxy(
            Authentication authentication,
            @RequestBody @Valid ProxyConfigHolder proxy) {
        proxySourceQueueHandler.addProxy(authentication.getName(), proxy);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('WORKER' or hasRole('ADMIN'))")
    public ResponseEntity<ProxyConfigHolder> getProxy(
            @NotBlank(message = "username can't be blank")
            @PathVariable String username) {
        var proxy = proxySourceQueueHandler.getProxy(username);
        logger.info("retrieved proxy: {}", proxy);
        return new ResponseEntity<>(proxy, HttpStatus.OK);
    }
}
