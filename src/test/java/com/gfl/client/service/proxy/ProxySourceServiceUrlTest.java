package com.gfl.client.service.proxy;

import com.gfl.client.service.proxy.source.ProxySourceService;
import com.gfl.client.service.proxy.source.ProxySourceServiceUrl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "proxy.source.type=url")
public class ProxySourceServiceUrlTest {

    @Autowired
    @Qualifier(value = "proxySourceServiceUrl")
    private ProxySourceService proxySourceService;

    @Test
    public void testFileProxySourceService() {
        assertInstanceOf(ProxySourceServiceUrl.class, proxySourceService);
    }

    // todo: add tests for ProxySourceServiceUrl
}

