package com.gfl.client.service.proxy;

import com.gfl.client.service.proxy.source.ProxySourceService;
import com.gfl.client.service.proxy.source.ProxySourceServiceFile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "proxy.source.type=file")
class ProxySourceServiceFileTest {

    @Autowired
    @Qualifier(value = "proxySourceServiceFile")
    private ProxySourceService proxySourceService;

    @Test
    void testFileProxySourceService() {
        assertInstanceOf(ProxySourceServiceFile.class, proxySourceService);
    }

    // todo: add tests for ProxySourceServiceFile
}
