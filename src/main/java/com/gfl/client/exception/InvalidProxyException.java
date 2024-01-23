package com.gfl.client.exception;

import com.gfl.client.model.ProxyConfigHolder;

public class InvalidProxyException extends RuntimeException {
    public InvalidProxyException() {
    }

    public InvalidProxyException(ProxyConfigHolder proxy) {
        super("Invalid proxy: " + proxy.toString());
    }
}
