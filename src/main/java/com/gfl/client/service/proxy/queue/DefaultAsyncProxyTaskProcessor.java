package com.gfl.client.service.proxy.queue;

import com.gfl.client.service.proxy.source.ProxySourceService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class DefaultAsyncProxyTaskProcessor implements AsyncProxyQueueTaskProcessor {

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
                .forEach(proxySourceQueueHandler::addProxy);
    }
}
