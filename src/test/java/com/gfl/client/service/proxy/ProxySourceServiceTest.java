package com.gfl.client.service.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfl.client.mapper.ProxyMapper;
import com.gfl.client.mapper.ProxyMapperImpl;
import com.gfl.client.util.file.FileJsonParser;
import com.gfl.client.util.file.FileParser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProxySourceServiceTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(TestConfig.class);

    @Test
    void whenHttpThenProxySourceServiceUrlIsUsed() {
        this.contextRunner.withPropertyValues("proxy.source.type=http")
                          .run(context -> {
                              ProxySourceService proxySourceService = context.getBean(ProxySourceService.class);
                              assertEquals(1, context.getBeanNamesForType(ProxySourceService.class).length);
                              assertTrue(proxySourceService instanceof ProxySourceServiceUrl);
                              assertTrue(context.containsBean("proxySourceServiceUrl"));
                              assertFalse(proxySourceService.getAllProxyConfigs().isEmpty());
                          });
    }

    @Test
    void whenFileThenProxySourceServiceFileIsUsed() {
        this.contextRunner.withPropertyValues("proxy.source.type=file")
                          .run(context -> {
                              ProxySourceService proxySourceService = context.getBean(ProxySourceService.class);
                              assertTrue(proxySourceService instanceof ProxySourceServiceFile);
                              assertEquals(1, context.getBeanNamesForType(ProxySourceService.class).length);
                              assertTrue(context.containsBean("proxySourceServiceFile"));
                              assertFalse(proxySourceService.getAllProxyConfigs().isEmpty());
                          });
    }

    static class TestConfig {

        @Bean
        public ProxyMapper proxyMapper() {
            return new ProxyMapperImpl();
        }
        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }

        @Bean public FileParser fileParser(){
            return new FileJsonParser(new ObjectMapper(), new DefaultResourceLoader());
        }

        @Bean
        @ConditionalOnProperty(name = "proxy.source.type", havingValue = "http")
        public ProxySourceService proxySourceServiceUrl(
                RestTemplate restTemplate, ProxyMapper proxyMapper) {
            return new ProxySourceServiceUrl(restTemplate, proxyMapper);
        }

        @Bean
        @ConditionalOnProperty(name = "proxy.source.type", havingValue = "file")
        public ProxySourceService proxySourceServiceFile(FileParser fileParser) {
            return new ProxySourceServiceFile(fileParser);
        }
    }
}
