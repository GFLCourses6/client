package com.gfl.client.service.proxy.queue;

import com.gfl.client.model.ProxyConfigHolder;

public interface ProxySourceQueueHandler {

    void addProxy(ProxyConfigHolder proxyConfigHolder);
    void addProxy(String queueName, ProxyConfigHolder proxy);
    ProxyConfigHolder getProxy(String username);
    void removeInvalidProxies();
}
