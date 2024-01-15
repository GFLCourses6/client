package com.gfl.client.service.proxy.queue;

import com.gfl.client.model.ProxyConfigHolder;
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
    // todo: add ProxyValidationService dependency

    public DefaultProxySourceQueueHandler(@Lazy AsyncProxyQueueTaskProcessor asyncProxyQueueTaskExecutor) {
        this.asyncProxyQueueTaskExecutor = asyncProxyQueueTaskExecutor;
        this.queues = new ConcurrentHashMap<>();
        this.queueLocks = new ConcurrentHashMap<>();
        this.queues.put(COMMON_QUEUE, new LinkedBlockingQueue<>());
        this.queueLocks.put(COMMON_QUEUE, new ReentrantLock());
    }

    @Override
    @Scheduled(cron = "${cron.remove.proxies}")
    public void removeInvalidProxies() {
        queues.forEach((key, queue) ->
                queue.removeIf(p -> false)); // todo: remove if proxy is invalid
    }

    @Override
    public void addCommonProxy(ProxyConfigHolder proxyConfigHolder) {
        addProxy(COMMON_QUEUE, proxyConfigHolder);
    }

    @Override
    public void addProxy(String queueName, ProxyConfigHolder proxy) {
        // todo: validate proxy, if it's invalid, throw an exception
        queueLocks.computeIfAbsent(queueName, key -> new ReentrantLock());
        Queue<ProxyConfigHolder> queue = queues.computeIfAbsent(
                queueName, key -> new LinkedBlockingQueue<>());

        if (!proxy.isUseAlways() && proxy.getUseTimes() == null) {
            proxy.setUseTimes(1L);
        }
        queue.add(proxy);
    }

    @Override
    public ProxyConfigHolder getProxy(String username) {
        Queue<ProxyConfigHolder> commonQueue = getCommonQueue();
        Lock commonLock = getCommonLock();

        // retrieve user's proxy, if it doesn't exist, retrieve common proxy, else return null
        return getUserProxy(username).orElseGet(() ->
                getProxy(commonQueue, commonLock).orElseGet(()-> {
                    asyncProxyQueueTaskExecutor.fillCommonQueue();
                    return null;
                }));
    }

    private Optional <ProxyConfigHolder> getUserProxy(String username) {
        var queue = queues.get(username);
        var lock = queueLocks.get(username);
        return (queue != null && !queue.isEmpty()) ? getProxy(queue, lock) : Optional.empty();
    }

    private Optional<ProxyConfigHolder> getProxy(Queue<ProxyConfigHolder> queue, Lock lock) {
        lock.lock();
        try {
            var optionalProxy = queue.stream()
                    .filter(p -> true) // todo: add proxy validation
                    .findFirst();

            optionalProxy.ifPresent(proxy -> remove(proxy, queue));
            return optionalProxy;
        } finally {
            lock.unlock();
        }
    }

    private void remove(ProxyConfigHolder proxyConfigHolder, Queue<ProxyConfigHolder> queue) {
        if (!proxyConfigHolder.isUseAlways()) {
            proxyConfigHolder.countDownUseTimes();
            if (proxyConfigHolder.getUseTimes() == 0) {
                queue.remove(proxyConfigHolder);
            }
        }
    }

    private BlockingQueue<ProxyConfigHolder> getCommonQueue() {
        return queues.get(COMMON_QUEUE);
    }

    private Lock getCommonLock() {
        return queueLocks.get(COMMON_QUEUE);
    }

}
