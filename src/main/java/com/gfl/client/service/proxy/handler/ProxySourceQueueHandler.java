package com.gfl.client.service.proxy.handler;

import com.gfl.client.model.ProxyConfigHolder;

public interface ProxySourceQueueHandler {

    void addProxy(ProxyConfigHolder proxy);

    ProxyConfigHolder getProxy(String username);
}
