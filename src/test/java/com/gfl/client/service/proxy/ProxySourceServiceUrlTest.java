package com.gfl.client.service.proxy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "proxy.source.type=http")
public class ProxySourceServiceUrlTest {

    @Autowired
    private ProxySourceService proxySourceService;

    @Test
    public void testFileProxySourceService() {
        assertInstanceOf(ProxySourceServiceUrl.class, proxySourceService);
    }

    // todo: add tests for ProxySourceServiceUrl
}

