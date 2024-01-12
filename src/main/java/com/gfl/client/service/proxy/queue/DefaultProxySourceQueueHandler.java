package com.gfl.client.service.proxy.queue;

import com.gfl.client.model.ProxyConfigHolder;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@Service
public class DefaultProxySourceQueueHandler implements ProxySourceQueueHandler {

    private final String COMMON_QUEUE = "common";
    private final Map<String, BlockingQueue<ProxyConfigHolder>> queues;
    private final AsyncProxyQueueTaskProcessor asyncProxyQueueTaskExecutor;
    // todo: add ProxyValidationService dependency

    public DefaultProxySourceQueueHandler(@Lazy AsyncProxyQueueTaskProcessor asyncProxyQueueTaskExecutor) {
        this.asyncProxyQueueTaskExecutor = asyncProxyQueueTaskExecutor;
        this.queues = new HashMap<>();
        this.queues.put(COMMON_QUEUE, new LinkedBlockingDeque<>());
    }

    @Override
    @Scheduled(cron = "0 * * * * *")
    public void removeInvalidProxies() {
        queues.forEach((key, queue) ->
                queue.removeIf(p -> false)); // todo: remove if proxy is invalid
    }

    @Override
    public void addProxy(ProxyConfigHolder proxyConfigHolder) {
        addProxy(COMMON_QUEUE, proxyConfigHolder);
    }

    @Override
    public void addProxy(String queueName, ProxyConfigHolder proxy) {
        Queue<ProxyConfigHolder> queue = queues.computeIfAbsent(
                queueName, key -> new LinkedBlockingDeque<>());
        queue.add(proxy);
    }

    @Override
    public ProxyConfigHolder getProxy(String username) {
        Queue<ProxyConfigHolder> commonQueue = getCommonQueue();

        // retrieve user's proxy, if it doesn't exist, retrieve common proxy
        return getUserProxy(username).orElseGet(() ->
                getProxy(commonQueue).orElseGet(()-> {
                    asyncProxyQueueTaskExecutor.fillCommonQueue();
                    return new ProxyConfigHolder();
                }));
    }

    private Optional <ProxyConfigHolder> getUserProxy(String username) {
        var queue = queues.get(username);
        return (queue != null && !queue.isEmpty()) ? getProxy(queue) : Optional.empty();
    }

    private Optional<ProxyConfigHolder> getProxy(Queue<ProxyConfigHolder> queue) {
        return queue.stream()
                .filter(p -> true) // todo: add proxy validation
                .findFirst();
    }

    private BlockingQueue<ProxyConfigHolder> getCommonQueue() {
        return queues.get(COMMON_QUEUE);
    }

}
