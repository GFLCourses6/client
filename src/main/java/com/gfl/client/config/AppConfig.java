package com.gfl.client.config;

import com.gfl.client.mapper.ProxyMapper;
import com.gfl.client.service.proxy.source.ProxySourceService;
import com.gfl.client.service.proxy.source.ProxySourceServiceFile;
import com.gfl.client.service.proxy.source.ProxySourceServiceUrl;
import com.gfl.client.util.file.FileParser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = "com.gfl.client.util.file")
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean(value = "proxySourceServiceUrlConfig")
    @ConditionalOnProperty(name = "proxy.source.type", havingValue = "url")
    public ProxySourceService proxySourceServiceUrl(RestTemplate restTemplate, ProxyMapper proxyMapper) {
        return new ProxySourceServiceUrl(restTemplate, proxyMapper);
    }

    @Bean(value = "proxySourceServiceFile")
    @ConditionalOnProperty(name = "proxy.source.type", havingValue = "file")
    public ProxySourceService proxySourceServiceFile(FileParser fileParser) {
        return new ProxySourceServiceFile(fileParser);
    }
}
