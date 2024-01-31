package com.gfl.client.service.proxy.validation;

import com.gfl.client.model.ProxyConfigHolder;
import com.gfl.client.model.ProxyCredentials;
import com.gfl.client.model.ProxyNetworkConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class DefaultProxyValidationServiceIntegrationTest {

    @Autowired
    private DefaultProxyValidationService proxyValidationService;

    @Test
    public void testInvalidProxy() {
        ProxyConfigHolder proxyConfigHolder = new ProxyConfigHolder(
                new ProxyNetworkConfig("193.42.255.13", 6503),
                new ProxyCredentials("username", "password"), null, false
        );
        assertTrue(proxyValidationService.isInvalidProxy(proxyConfigHolder));
    }
}
