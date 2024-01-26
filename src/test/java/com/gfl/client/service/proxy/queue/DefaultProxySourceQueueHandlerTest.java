package com.gfl.client.service.proxy.queue;

import com.gfl.client.exception.InvalidProxyException;
import com.gfl.client.model.ProxyConfigHolder;
import com.gfl.client.model.ProxyCredentials;
import com.gfl.client.model.ProxyNetworkConfig;
import com.gfl.client.service.proxy.validation.ProxyValidationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

class DefaultProxySourceQueueHandlerTest {

    @Mock
    private AsyncProxyQueueTaskProcessor asyncProxyQueueTaskExecutor;

    @Mock
    private ProxyValidationService proxyValidationService;

    @Mock
    private AsyncProxyQueueTaskProcessor asyncProxyQueueTaskProcessor;

    @InjectMocks
    private DefaultProxySourceQueueHandler proxySourceQueueHandler;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void cleanUp() throws Exception {
        closeable.close();
    }

    @AfterEach
    void cleanup() {
        // Clear queues or perform other cleanup tasks
        proxySourceQueueHandler.getCommonQueue().clear();
    }


    @Test
    public void testAddCommonProxy() {
        ProxyConfigHolder proxyConfigHolder = new ProxyConfigHolder();
        // Mock the validation service to consider the proxy as valid
        when(proxyValidationService.isInvalidProxy(proxyConfigHolder)).thenReturn(false);
        // Add a valid proxy to the common queue
        proxySourceQueueHandler.addCommonProxy(proxyConfigHolder);

        // Verify that the proxy was added to the common queue
        assertTrue(proxySourceQueueHandler.getCommonQueue().contains(proxyConfigHolder));
    }

    @Test
    public void testAddProxyWithInvalidProxy() {
        ProxyConfigHolder proxyConfigHolder = new ProxyConfigHolder();
        when(proxyValidationService.isInvalidProxy(proxyConfigHolder)).thenReturn(true);

        assertThrows(InvalidProxyException.class, () -> proxySourceQueueHandler.addCommonProxy(proxyConfigHolder));
    }

    @Test
    public void testGetProxy() {
        ProxyConfigHolder proxyConfigHolder = new ProxyConfigHolder();
        when(proxyValidationService.isValidProxy(proxyConfigHolder)).thenReturn(true);
        proxySourceQueueHandler.addCommonProxy(proxyConfigHolder);

        ProxyConfigHolder retrievedProxy = proxySourceQueueHandler.getProxy("testUser");

        // Verify that the proxy was retrieved from the common queue
        assertEquals(proxyConfigHolder, retrievedProxy);
    }

    @Test
    void addCommonProxyInvalidProxy() {
        ProxyConfigHolder invalidProxy = new ProxyConfigHolder();

        // Mock the validation service to consider the proxy as invalid
        when(proxyValidationService.isInvalidProxy(invalidProxy)).thenReturn(true);

        // Adding an invalid proxy should throw InvalidProxyException
        assertThrows(InvalidProxyException.class,
                () -> proxySourceQueueHandler.addCommonProxy(invalidProxy)
        );
    }

    @Test
    public void testRemoveInvalidProxies() {
        // Mock data
        ProxyConfigHolder validProxy = new ProxyConfigHolder(new ProxyNetworkConfig("hostname", 8080), new ProxyCredentials("username", "password"), 1L, true);
        ProxyConfigHolder invalidProxy = new ProxyConfigHolder(new ProxyNetworkConfig("hostname", 8080), new ProxyCredentials("username", "password"), 0L, false);

        when(proxyValidationService.isInvalidProxy(validProxy)).thenReturn(false);
        when(proxyValidationService.isInvalidProxy(invalidProxy)).thenReturn(true);

        proxySourceQueueHandler.addCommonProxy(validProxy);
        // Add an invalid proxy to the common queue
        assertThrows(InvalidProxyException.class, () -> {
            proxySourceQueueHandler.addCommonProxy(invalidProxy);
        });

        proxySourceQueueHandler.removeInvalidProxies();

        // Verify that the invalid proxy was removed
        assertFalse(proxySourceQueueHandler.getCommonQueue().contains(invalidProxy));
        // Verify that the valid proxy remains
        assertTrue(proxySourceQueueHandler.getCommonQueue().contains(validProxy));
    }

}