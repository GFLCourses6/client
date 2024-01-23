package com.gfl.client.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProxyNetworkConfigTest {
    private ProxyNetworkConfig proxyNetworkConfig;
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

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

        // Consistency: Multiple invocations should return the same hash code
        int firstHashCode = proxyNetworkConfig.hashCode();
        int secondHashCode = proxyNetworkConfig.hashCode();
        assertEquals(firstHashCode, secondHashCode);

        // Equality: If two objects are equal, their hash codes should be the same
        ProxyNetworkConfig equalNetworkConfig = new ProxyNetworkConfig("hostname", 8080);
        assertEquals(proxyNetworkConfig, equalNetworkConfig);
        assertEquals(proxyNetworkConfig.hashCode(), equalNetworkConfig.hashCode());
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

    @Test
    @DisplayName("Test validation fails for null ProxyNetworkConfig")
    void testValidationFailsForNullProxyNetworkConfig() {
        ProxyNetworkConfig networkConfig = new ProxyNetworkConfig(null, null);
        Set<ConstraintViolation<ProxyNetworkConfig>> violations = validator.validate(networkConfig);

        assertEquals(2, violations.size());

        ConstraintViolation<ProxyNetworkConfig> hostnameViolation = violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals("hostname"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected violation for null hostname not found"));
        assertEquals("hostname can't be blank", hostnameViolation.getMessage());

        ConstraintViolation<ProxyNetworkConfig> portViolation = violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals("port"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected violation for null port not found"));
        assertEquals("port can't be null", portViolation.getMessage());
    }

    @Test
    @DisplayName("Test validation fails for more size than should be in ProxyNetworkConfig")
    void testValidationFailsForSizeMoreProxyNetworkConfig() {
        ProxyNetworkConfig networkConfig = new ProxyNetworkConfig("ThisIsAHostnameWithMoreThanFiftyCharactersInThisSentence", 65536);
        Set<ConstraintViolation<ProxyNetworkConfig>> violations = validator.validate(networkConfig);

        assertEquals(2, violations.size());

        ConstraintViolation<ProxyNetworkConfig> hostnameViolation = violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals("hostname"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected violation for 50+ hostname letters"));
        assertEquals("Name must be between 1 and 50 characters", hostnameViolation.getMessage());

        ConstraintViolation<ProxyNetworkConfig> portViolation = violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals("port"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected violation for port more than 65535"));
        assertEquals("port can't be more than 65535", portViolation.getMessage());
    }

    @Test
    @DisplayName("Test validation fails for less size than should be in ProxyNetworkConfig")
    void testValidationFailsForSizeLessProxyNetworkConfig() {
        ProxyNetworkConfig networkConfig = new ProxyNetworkConfig(null, -1);
        Set<ConstraintViolation<ProxyNetworkConfig>> violations = validator.validate(networkConfig);

        ConstraintViolation<ProxyNetworkConfig> hostnameViolation =
                violations.stream()
                          .filter(v -> v.getPropertyPath().toString().equals("hostname"))
                          .findFirst()
                          .orElseThrow(() -> new AssertionError("Expected violation for 50+ hostname letters"));

        String expectedMessage = "hostname can't be blank";
        assertEquals(expectedMessage, hostnameViolation.getMessage());

        ConstraintViolation<ProxyNetworkConfig> portViolation =
                violations.stream()
                          .filter(v -> v.getPropertyPath().toString().equals("port"))
                          .findFirst()
                          .orElseThrow(() -> new AssertionError("Expected violation for port more than 65535"));
        assertEquals("port can't be less than 0", portViolation.getMessage());
    }
}