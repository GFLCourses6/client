package com.gfl.client.service.proxy.queue;

import com.gfl.client.model.ProxyConfigHolder;
import com.gfl.client.service.proxy.source.ProxySourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class DefaultAsyncProxyTaskProcessor implements AsyncProxyQueueTaskProcessor {

    private final Logger logger = LoggerFactory.getLogger(DefaultAsyncProxyTaskProcessor.class);
    private final ProxySourceQueueHandler proxySourceQueueHandler;
    private final ProxySourceService proxySourceService;

    public DefaultAsyncProxyTaskProcessor(ProxySourceQueueHandler proxySourceQueueHandler,
                                          ProxySourceService proxySourceService) {
        this.proxySourceQueueHandler = proxySourceQueueHandler;
        this.proxySourceService = proxySourceService;
        fillCommonQueue();
    }

    @Override
    @Async
    public void fillCommonQueue() {
        proxySourceService.getAllProxyConfigs()
                .forEach(this::addProxy);
    }

    private void addProxy(ProxyConfigHolder proxy) {
        try {
            proxySourceQueueHandler.addCommonProxy(proxy);
        } catch (Exception e) {
            logger.error("Couldn't add the proxy: {}", proxy);
        }
    }
}
