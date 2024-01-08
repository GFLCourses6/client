package com.gfl.client.model.dto;

import lombok.Data;

@Data
public class ProxyConfigHolder{
    private final ProxyNetworkConfig proxyNetworkConfig;
    private final ProxyCredentials proxyCredentials;

}
