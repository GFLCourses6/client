package com.gfl.client;

import com.gfl.client.service.proxy.queue.AsyncProxyQueueTaskProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class QueueLoaderCommandRunner implements CommandLineRunner {

    private final AsyncProxyQueueTaskProcessor proxyQueueTaskProcessor;

    @Override
    public void run(String... args) {
        proxyQueueTaskProcessor.fillCommonQueue();
    }
}
