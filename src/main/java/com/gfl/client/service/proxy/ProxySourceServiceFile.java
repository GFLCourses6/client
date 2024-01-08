package com.gfl.client.service.proxy;


import com.gfl.client.exception.FileReadException;
import com.gfl.client.model.dto.ProxyConfigHolder;
import com.gfl.client.util.file.FileParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@Service
public class ProxySourceServiceFile implements ProxySourceService {
    private static final String PROXY_CONFIGS_PATH = "json/ProxyConfigs.json";
    private final FileParser fileParser;
    private final BlockingQueue<ProxyConfigHolder> proxyConfigHolders;
    @Autowired
    public ProxySourceServiceFile(FileParser fileParser) {
        this.fileParser = fileParser;
        this.proxyConfigHolders = new LinkedBlockingDeque<>();
    }
    @Override
    public List<ProxyConfigHolder> getAllProxyConfigs(){
        try {
            return fileParser.getAllFromFile(PROXY_CONFIGS_PATH, ProxyConfigHolder.class);
        } catch (IOException e){
            throw new FileReadException(e.getMessage());
        }
    }
}
