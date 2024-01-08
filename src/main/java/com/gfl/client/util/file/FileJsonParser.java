package com.gfl.client.util.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class FileJsonParser implements FileParser {

    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;

    @Autowired
    public FileJsonParser(ObjectMapper objectMapper, ResourceLoader resourceLoader) {
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public <T> List<T> getAllFromFile(String filePath, Class<T> type) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + filePath);
        return objectMapper.readValue(resource.getInputStream(),
                                      objectMapper.getTypeFactory().constructCollectionType(List.class, type));
    }

    @Override
    public <T> T getFromFile(String filePath, Class<T> type) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + filePath);
        return objectMapper.readValue(resource.getInputStream(), type);
    }
}
