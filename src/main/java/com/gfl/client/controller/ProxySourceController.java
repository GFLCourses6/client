package com.gfl.client.controller;

import com.gfl.client.model.ProxyConfigHolder;
import com.gfl.client.service.proxy.queue.ProxySourceQueueHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/proxy")
@Api(value = "Operations pertaining to proxy sources")
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Invalid input provided"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Proxy configuration not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
})
public class ProxySourceController {

    private final ProxySourceQueueHandler proxySourceQueueHandler;
    private final Logger logger = LoggerFactory.getLogger(ProxySourceController.class);

    @PostMapping
    @ApiOperation(value = "Add a proxy configuration",
            notes = "Endpoint for adding a proxy configuration. Requires authentication.",
            response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added the proxy"),
    })
    public ResponseEntity<Void> addProxy(
            Authentication authentication,
            @ApiParam(value = "Proxy config holder object", required = true)
            @RequestBody @Valid ProxyConfigHolder proxy) {
        proxySourceQueueHandler.addProxy(authentication.getName(), proxy);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('WORKER')")
    @ApiOperation(value = "Get proxy configuration by username",
            notes = "Endpoint for retrieving a proxy configuration by username. Requires 'WORKER' role.",
            response = ProxyConfigHolder.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proxy configuration retrieved successfully"),
    })
    public ResponseEntity<ProxyConfigHolder> getProxy(
            @ApiParam(value = "Username for which proxy needs to be retrieved", required = true)
            @NotBlank(message = "username can't be blank")
            @PathVariable("username") String username) {
        var proxy = proxySourceQueueHandler.getProxy(username);
        logger.info("retrieved proxy: {}", proxy);
        return new ResponseEntity<>(proxy, HttpStatus.OK);
    }
}
