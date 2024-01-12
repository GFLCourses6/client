package com.gfl.client.service.proxy.queue;

import org.springframework.scheduling.annotation.Async;

public interface AsyncProxyQueueTaskProcessor {

    @Async
    void fillCommonQueue();
}
