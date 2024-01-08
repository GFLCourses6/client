package com.gfl.client.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProxyConfigHolder {

    @Valid
    @NotNull
    private ProxyNetworkConfig proxyNetworkConfig;

    @Valid
    private ProxyCredentials proxyCredentials;
}
