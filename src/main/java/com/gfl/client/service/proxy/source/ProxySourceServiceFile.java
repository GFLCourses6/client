package com.gfl.client.service.proxy.source;


import com.gfl.client.exception.FileReadException;
import com.gfl.client.model.ProxyConfigHolder;
import com.gfl.client.util.file.FileParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProxySourceServiceFile implements ProxySourceService {

    @Value("${proxy.filepath}")
    private String proxyFilePath;
    private final FileParser fileParser;

    @Override
    public List<ProxyConfigHolder> getAllProxyConfigs(){
        try {
            return fileParser.getAllFromFile(proxyFilePath, ProxyConfigHolder.class);
        } catch (IOException e){
            throw new FileReadException(e.getMessage());
        }
    }
}
