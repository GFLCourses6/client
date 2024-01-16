package com.gfl.client.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ProxyCredentialsTest {
    private ProxyCredentials proxyCredentials;

    @BeforeEach
    void setUp(){
        proxyCredentials = new ProxyCredentials("username1", "password1");
    }

    @Test
    @DisplayName("Test empty constructor with null password and username")
    void testEmptyConstructor(){
        ProxyCredentials emptyProxyCredentials = new ProxyCredentials();

        assertNull(emptyProxyCredentials.getUsername());
        assertNull(emptyProxyCredentials.getPassword());
    }

    @Test
    @DisplayName("Test getting password from ProxyCredentials object")
    void testProxyCredentialsGetPassword(){
        assertEquals("password1", proxyCredentials.getPassword());
    }

    @Test
    @DisplayName("Test getting username from ProxyCredentials object")
    void testProxyCredentialsGetUsername(){
        assertEquals("username1", proxyCredentials.getUsername());
    }

    @Test
    @DisplayName("Test setting username from ProxyCredentials object")
    void testProxyCredentialsSetUsername(){
        assertEquals("username1", proxyCredentials.getUsername());
        proxyCredentials.setUsername("username");
        assertEquals("username", proxyCredentials.getUsername());
    }

    @Test
    @DisplayName("Test setting password from ProxyCredentials object")
    void testProxyCredentialsSePassword(){
        assertEquals("password1", proxyCredentials.getPassword());
        proxyCredentials.setPassword("password");
        assertEquals("password", proxyCredentials.getPassword());
    }

    @Test
    @DisplayName("Test equality between ProxyCredentials objects")
    void testEquals() {
        ProxyCredentials SameProxyCredentials = new ProxyCredentials("username1", "password1");
        assertEquals(proxyCredentials, SameProxyCredentials);
    }

    @Test
    @DisplayName("Test not equality between ProxyCredentials objects")
    void testEqualsFailed() {
        ProxyCredentials SameProxyCredentials = new ProxyCredentials("username", "password");
        assertNotEquals(proxyCredentials, SameProxyCredentials);
    }

    @Test
    @DisplayName("Test hashCode method for ProxyCredentials object")
    void testHashCode() {
        int expectedHashCode = proxyCredentials.hashCode();
        assertEquals(expectedHashCode, proxyCredentials.hashCode());
    }

    @Test
    @DisplayName("Test toString method for ProxyCredentials object")
    void testToString() {
        String expectedString = "ProxyCredentials(username=username1, password=password1)";
        assertEquals(expectedString, proxyCredentials.toString());
    }

    @Test
    @DisplayName("Test equality and hashCode for ProxyCredentials objects with the same values")
    void testEqualsAndHashCode() {
        ProxyCredentials credentials = new ProxyCredentials();
        credentials.setPassword("password1");
        credentials.setUsername("username1");

        assertEquals(proxyCredentials, credentials);
        assertEquals(proxyCredentials.hashCode(), credentials.hashCode());
    }

}