package com.gfl.client.service.proxy;

import com.gfl.client.service.proxy.source.ProxySourceService;
import com.gfl.client.service.proxy.source.ProxySourceServiceFile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "proxy.source.type=file")
public class ProxySourceServiceFileTest {

    @Autowired
    private ProxySourceService proxySourceService;

    @Test
    public void testFileProxySourceService() {
        assertInstanceOf(ProxySourceServiceFile.class, proxySourceService);
    }

    // todo: add tests for ProxySourceServiceFile
}
