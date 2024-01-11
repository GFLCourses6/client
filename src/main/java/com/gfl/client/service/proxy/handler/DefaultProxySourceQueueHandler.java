package com.gfl.client.service.proxy.handler;

import com.gfl.client.model.ProxyConfigHolder;
import com.gfl.client.service.proxy.ProxySourceService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class DefaultProxySourceQueueHandler implements ProxySourceQueueHandler{

    private final String COMMON_QUEUE = "common";
    private final Map<String, BlockingQueue<ProxyConfigHolder>> queues;
    private  final ProxySourceService proxySourceService;
    // todo: add ProxyValidationService dependency

    public DefaultProxySourceQueueHandler(ProxySourceService proxySourceService) {
        this.proxySourceService = proxySourceService;
        this.queues = new HashMap<>();
        this.queues.put(COMMON_QUEUE, new LinkedBlockingDeque<>());
        fillCommonQueue();

    }

    public Map<String, BlockingQueue<ProxyConfigHolder>> getQueues(){
        return queues;
    }
    @Override
    public void addProxy(ProxyConfigHolder proxy){
        // todo: validate proxy, if it's invalid, throw an exception
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Queue<ProxyConfigHolder> queue = queues.computeIfAbsent(
                authentication.getName(), key -> new LinkedBlockingDeque<>());
        queue.add(proxy);
    }
    @Override
    public ProxyConfigHolder getProxy(String username){
        Queue<ProxyConfigHolder> commonQueue = getCommonQueue();

        return getUserProxy(username).orElseGet(() ->
                getProxy(commonQueue).orElseGet(()-> {
                    fillCommonQueue();
                    return new ProxyConfigHolder();
                }));
    }

    protected void fillCommonQueue(){
        getCommonQueue().addAll(proxySourceService.getAllProxyConfigs());
    }

    private Optional <ProxyConfigHolder> getUserProxy(String username){
        var queue = queues.get(username);
        return (queue != null && !queue.isEmpty()) ? getProxy(queue) : Optional.empty();
    }

    private Optional<ProxyConfigHolder> getProxy(Queue<ProxyConfigHolder> queue){
        return queue.stream()
                .filter(p -> true) // todo: add proxy validation
                .findFirst();
    }

    private BlockingQueue<ProxyConfigHolder> getCommonQueue(){
        return queues.get(COMMON_QUEUE);
    }

}
