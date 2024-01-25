package com.gfl.client.service.proxy.source;

import com.gfl.client.model.ProxyConfigHolder;

import java.util.List;

public interface ProxySourceService {
    List<ProxyConfigHolder> getAllProxyConfigs();
}
