package com.gfl.client.service.proxy.queue;

import com.gfl.client.model.ProxyConfigHolder;

public interface ProxySourceQueueHandler {

    void addCommonProxy(ProxyConfigHolder proxyConfigHolder);
    void addProxy(String queueName, ProxyConfigHolder proxyConfigHolder);
    ProxyConfigHolder getProxy(String username);
    void removeInvalidProxies();
}
