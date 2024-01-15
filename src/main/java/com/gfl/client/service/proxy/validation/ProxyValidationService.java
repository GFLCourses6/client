package com.gfl.client.service.proxy.validation;

import com.gfl.client.model.ProxyConfigHolder;

public interface ProxyValidationService {
    boolean isValidProxy(ProxyConfigHolder proxyConfig);
}

