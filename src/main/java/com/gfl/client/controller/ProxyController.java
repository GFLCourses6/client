package com.gfl.client.controller;

import com.gfl.client.model.ProxyConfigHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

@Api(value = "Operations pertaining to proxy sources")
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Invalid input provided"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Proxy configuration not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
})
public interface ProxyController {

    @ApiOperation(value = "Add a proxy configuration",
            notes = "Endpoint for adding a proxy configuration. Requires authentication.",
            response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added the proxy"),
    })
    ResponseEntity<Void> addProxy(
            Authentication authentication,
            @ApiParam(value = "Proxy config holder object", required = true)
            @Valid ProxyConfigHolder proxy);

    @ApiOperation(value = "Get proxy configuration by username",
            notes = "Endpoint for retrieving a proxy configuration by username. Requires 'WORKER' role.",
            response = ProxyConfigHolder.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proxy configuration retrieved successfully"),
    })
    ResponseEntity<ProxyConfigHolder> getProxy(
            @ApiParam(value = "Username for which proxy needs to be retrieved", required = true)
            @NotBlank(message = "username can't be blank")
            String username);

}

