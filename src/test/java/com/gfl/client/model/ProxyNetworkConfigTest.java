package com.gfl.client.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.parameters.P;

import static org.junit.jupiter.api.Assertions.*;

class ProxyNetworkConfigTest {
    private ProxyNetworkConfig proxyNetworkConfig;

    @BeforeEach
    void setUp(){
        proxyNetworkConfig = new ProxyNetworkConfig("hostname", 8080);
    }

    @Test
    @DisplayName("Test empty constructor with null hostname and port")
    void testEmptyConstructor(){
        ProxyNetworkConfig config = new ProxyNetworkConfig();

        assertNull(config.getHostname());
        assertNull(config.getPort());
    }

    @Test
    @DisplayName("Test getting hostname from ProxyNetworkConfig object")
    void testProxyCredentialsGetHostname(){
        assertEquals("hostname", proxyNetworkConfig.getHostname());
    }

    @Test
    @DisplayName("Test getting port from ProxyNetworkConfig object")
    void testProxyCredentialsGetPort(){
        assertEquals(8080, proxyNetworkConfig.getPort());
    }

    @Test
    @DisplayName("Test setting hostname from ProxyNetworkConfig object")
    void testProxyCredentialsSetHostname(){
        assertEquals("hostname", proxyNetworkConfig.getHostname());
        proxyNetworkConfig.setHostname("HOST");
        assertEquals("HOST", proxyNetworkConfig.getHostname());
    }

    @Test
    @DisplayName("Test setting port from ProxyNetworkConfig object")
    void testProxyCredentialsSetPort(){
        assertEquals(8080, proxyNetworkConfig.getPort());
        proxyNetworkConfig.setPort(5000);
        assertEquals(5000, proxyNetworkConfig.getPort());

    }

    @Test
    @DisplayName("Test equality between ProxyNetworkConfig objects")
    void testEquals() {
        ProxyNetworkConfig config1 = new ProxyNetworkConfig("hostname", 8080);
        assertEquals(proxyNetworkConfig, config1);
    }

    @Test
    @DisplayName("Test equality between ProxyNetworkConfig objects")
    void testEqualsFailed() {
        ProxyNetworkConfig config1 = new ProxyNetworkConfig("hostname1", 8081);
        assertNotEquals(proxyNetworkConfig, config1);
    }

    @Test
    @DisplayName("Test hashCode method for ProxyNetworkConfig object")
    void testHashCode() {
        int expected = proxyNetworkConfig.hashCode();
        assertEquals(expected, proxyNetworkConfig.hashCode());
    }

    @Test
    @DisplayName("Test toString method for ProxyNetworkConfig object")
    void testToString() {
        String expected = "ProxyNetworkConfig(hostname=hostname, port=8080)";
        assertEquals(expected, proxyNetworkConfig.toString());
    }

    @Test
    @DisplayName("Test equality and hashCode for ProxyNetworkConfig objects with the same values")
    void testEqualsAndHashCode() {
        ProxyNetworkConfig config = new ProxyNetworkConfig();
        config.setHostname("hostname");
        config.setPort(8080);

        assertEquals(proxyNetworkConfig, config);
        assertEquals(proxyNetworkConfig.hashCode(), config.hashCode());

    }

}