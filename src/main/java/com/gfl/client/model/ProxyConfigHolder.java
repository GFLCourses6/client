package com.gfl.client.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProxyConfigHolder {

    @Valid
    @NotNull(message = "proxy network configuration can't be null")
    private ProxyNetworkConfig proxyNetworkConfig;

    @Valid
    private ProxyCredentials proxyCredentials;
}
