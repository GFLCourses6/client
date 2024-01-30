package com.gfl.client.service.proxy.queue;

import com.gfl.client.exception.InvalidProxyException;
import com.gfl.client.model.ProxyConfigHolder;
import com.gfl.client.service.proxy.validation.ProxyValidationService;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class DefaultProxySourceQueueHandler implements ProxySourceQueueHandler {

    private static final String COMMON_QUEUE = "common";
    private final Map<String, BlockingQueue<ProxyConfigHolder>> queues;
    private final Map<String, Lock> queueLocks;
    private final AsyncProxyQueueTaskProcessor asyncProxyQueueTaskExecutor;
    private final ProxyValidationService proxyValidationService;

    public DefaultProxySourceQueueHandler(@Lazy AsyncProxyQueueTaskProcessor asyncProxyQueueTaskExecutor,
                                          ProxyValidationService proxyValidationService) {
        this.asyncProxyQueueTaskExecutor = asyncProxyQueueTaskExecutor;
        this.proxyValidationService = proxyValidationService;
        this.queues = new ConcurrentHashMap<>();
        this.queueLocks = new ConcurrentHashMap<>();
        this.queues.put(COMMON_QUEUE, new LinkedBlockingQueue<>());
        this.queueLocks.put(COMMON_QUEUE, new ReentrantLock());
    }

    @Override
    @Scheduled(cron = "${cron.remove.proxies}")
    public void removeInvalidProxies() {
        queues.forEach((key, queue) ->
                queue.removeIf(proxyValidationService::isInvalidProxy));
    }

    @Override
    public void addCommonProxy(ProxyConfigHolder proxyConfigHolder) {
        addProxy(COMMON_QUEUE, proxyConfigHolder);
    }

    @Override
    public void addProxy(String queueName, ProxyConfigHolder proxy) {
        if (proxyValidationService.isInvalidProxy(proxy)) {
            throw new InvalidProxyException(proxy);
        }
        // if use times haven't been set, initialize it with the default value of 1
        if (!proxy.isUseAlways() && proxy.getUseTimes() == null) {
            proxy.setUseTimes(1L);
        }
        // create lock and queue for the user if it doesn't exist and get them afterwards
        queueLocks.computeIfAbsent(queueName, key -> new ReentrantLock());
        Queue<ProxyConfigHolder> queue = queues.computeIfAbsent(
                queueName, key -> new LinkedBlockingQueue<>());

        queue.add(proxy);
    }

    @Override
    public ProxyConfigHolder getProxy(String username) {
        // retrieve user's proxy, if it doesn't exist, retrieve common proxy, else return null
        return getProxyFromQueue(username).orElseGet(() ->
                getProxyFromQueue(COMMON_QUEUE).orElseGet(()-> {
                    asyncProxyQueueTaskExecutor.fillCommonQueue();
                    return null;
                }));
    }

    private Optional<ProxyConfigHolder> getProxyFromQueue(String queueName) {
        var queue = queues.get(queueName);
        var lock = queueLocks.get(queueName);
        return (queue != null && !queue.isEmpty()) // try to find proxy if queue exists and it's not empty
                ? findProxy(queue, lock)
                : Optional.empty();
    }

    private Optional<ProxyConfigHolder> findProxy(Queue<ProxyConfigHolder> queue, Lock lock) {
        lock.lock();
        try {
            var optionalProxy = queue.stream()
                    .filter(proxyValidationService::isValidProxy)
                    .findFirst();

            optionalProxy.ifPresent(proxy -> tryRemove(proxy, queue));
            return optionalProxy;
        } finally {
            lock.unlock();
        }
    }

    private void tryRemove(ProxyConfigHolder proxyConfigHolder, Queue<ProxyConfigHolder> queue) {
        if (!proxyConfigHolder.isUseAlways()) {
            proxyConfigHolder.countDownUseTimes();
            if (proxyConfigHolder.getUseTimes() == 0) {
                queue.remove(proxyConfigHolder);
            }
        }
    }

    BlockingQueue<ProxyConfigHolder> getCommonQueue() {
        return queues.get(COMMON_QUEUE);
    }

    private Lock getCommonLock() {
        return queueLocks.get(COMMON_QUEUE);
    }
}
