package com.gfl.client;

import com.gfl.client.service.proxy.queue.AsyncProxyQueueTaskProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@RequiredArgsConstructor
public class ClientApplication implements CommandLineRunner {

    private final AsyncProxyQueueTaskProcessor proxyQueueTaskProcessor;

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Override
    public void run(String... args) {
        proxyQueueTaskProcessor.fillCommonQueue();
    }
}
