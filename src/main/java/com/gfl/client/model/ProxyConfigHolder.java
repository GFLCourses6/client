package com.gfl.client.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProxyConfigHolder {

    @Valid
    @NotNull(message = "proxy network configuration can't be null")
    private ProxyNetworkConfig proxyNetworkConfig;

    @Valid
    private ProxyCredentials proxyCredentials;

    public ProxyConfigHolder(){}

    public ProxyConfigHolder(
            ProxyNetworkConfig proxyNetworkConfig,
            ProxyCredentials proxyCredentials) {
        this.proxyNetworkConfig = proxyNetworkConfig;
        this.proxyCredentials = proxyCredentials;
    }
}
