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

class ProxyCredentialsTest {
    private ProxyCredentials proxyCredentials;
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

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

    @Test
    @DisplayName("Test validation fails for null ProxyCredentials")
    void testValidationFailsForNullProxyCredentials() {
        ProxyCredentials credentials = new ProxyCredentials(null, null);
        Set<ConstraintViolation<ProxyCredentials>> violations = validator.validate(credentials);

        assertEquals(2, violations.size());

        ConstraintViolation<ProxyCredentials> usernameViolation = violations.stream()
                .filter(v -> {
                    return v.getPropertyPath().toString().equals("username");
                })
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected violation for null username not found"));
        assertEquals("Username must not be blank", usernameViolation.getMessage());

        ConstraintViolation<ProxyCredentials> passwordViolation = violations.stream()
                .filter(v -> {
                    return v.getPropertyPath().toString().equals("password");
                })
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected violation for null password not found"));
        assertEquals("Password must not be blank", passwordViolation.getMessage());
    }

    @Test
    @DisplayName("Test validation fails for more size than should be in ProxyCredentials")
    void testValidationFailsForSizeMoreProxyCredentials() {
        ProxyCredentials credentials = new ProxyCredentials("ThisIsAHostnameWithMoreThanFiftyCharactersInThisSentence", "t#9mBc!RvXzP7sHqGjUyLxKvT1iFwOoD2eA3nZ4rY5e6d7u8i9t0yT1o2a3l4l5e6n7g8a9r0p!i@l5434t#9mBc!RvXzP7sHqGjUyLxKvT1iFwOoD2eA3nZ4rY5e6d7u8i9t0yT1o2a3l4l5e6n7g8a9r0p!i@l5434");
        Set<ConstraintViolation<ProxyCredentials>> violations = validator.validate(credentials);

        assertEquals(2, violations.size());

        ConstraintViolation<ProxyCredentials> usernameViolation = violations.stream()
                .filter(v -> {
                    return v.getPropertyPath().toString().equals("username");
                })
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected violation for 50+ hostname letters"));
        assertEquals("Username must be between 3 and 50 characters", usernameViolation.getMessage());

        ConstraintViolation<ProxyCredentials> passwordViolation = violations.stream()
                .filter(v -> {
                    return v.getPropertyPath().toString().equals("password");
                })
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected violation for password more than 65535"));
        assertEquals("Password must be between 6 and 100 characters", passwordViolation.getMessage());
    }

    @Test
    @DisplayName("Test validation fails for less size than should be in ProxyCredentials")
    void testValidationFailsForSizeLessProxyCredentials() {
        ProxyCredentials credentials = new ProxyCredentials("us", "1234");
        Set<ConstraintViolation<ProxyCredentials>> violations = validator.validate(credentials);

        ConstraintViolation<ProxyCredentials> usernameViolation = violations.stream()
                .filter(v -> {
                    return v.getPropertyPath().toString().equals("username");
                })
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected violation for 50+ hostname letters"));
        assertEquals("Username must be between 3 and 50 characters", usernameViolation.getMessage());

        ConstraintViolation<ProxyCredentials> passwordViolation = violations.stream()
                .filter(v -> {
                    return v.getPropertyPath().toString().equals("password");
                })
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected violation for port more than 65535"));
        assertEquals("Password must be between 6 and 100 characters", passwordViolation.getMessage());
    }

}