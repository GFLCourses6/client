package com.gfl.client.service.proxy.queue;

import com.gfl.client.model.ProxyConfigHolder;
import com.gfl.client.service.proxy.source.ProxySourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class DefaultAsyncProxyTaskProcessor implements AsyncProxyQueueTaskProcessor {

    private final Logger logger = LoggerFactory.getLogger(DefaultAsyncProxyTaskProcessor.class);
    private final ProxySourceQueueHandler proxySourceQueueHandler;
    private final ProxySourceService proxySourceService;
    private final Lock lock;

    public DefaultAsyncProxyTaskProcessor(ProxySourceQueueHandler proxySourceQueueHandler,
                                          ProxySourceService proxySourceService) {
        this.proxySourceQueueHandler = proxySourceQueueHandler;
        this.proxySourceService = proxySourceService;
        this.lock = new ReentrantLock();
    }

    @Override
    @Async
    public void fillCommonQueue() {
        if (lock.tryLock()) {
            try {
                proxySourceService.getAllProxyConfigs().forEach(this::addProxy);
            } finally {
                lock.unlock();
            }
        }
    }

    private void addProxy(ProxyConfigHolder proxy) {
        logger.info("Adding the proxy: {}", proxy);
        try {
            proxySourceQueueHandler.addCommonProxy(proxy);
        } catch (Exception e) {
            logger.error("Couldn't add the proxy: {}", proxy);
        }
    }
}
