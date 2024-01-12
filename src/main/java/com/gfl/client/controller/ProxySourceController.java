package com.gfl.client.controller;

import com.gfl.client.model.ProxyConfigHolder;
import com.gfl.client.service.proxy.queue.ProxySourceQueueHandler;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/proxy")
@RequiredArgsConstructor
public class ProxySourceController {

    private final ProxySourceQueueHandler proxySourceQueueHandler;

    @PostMapping
    public ResponseEntity<Void> addProxy(Authentication authentication,
                                         @RequestBody @Valid ProxyConfigHolder proxy) {
        proxySourceQueueHandler.addProxy(authentication.getName(), proxy);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    public ResponseEntity<ProxyConfigHolder> getProxy(@NotBlank(message = "username can't be blank")
                                                          @PathVariable("username") String username) {
        var proxy = proxySourceQueueHandler.getProxy(username);
        return new ResponseEntity<>(proxy, HttpStatus.OK);
    }
}
