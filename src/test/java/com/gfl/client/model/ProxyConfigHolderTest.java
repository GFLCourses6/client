package com.gfl.client.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ProxyConfigHolderTest {

    private ProxyConfigHolder proxyConfigHolder;
    private ProxyNetworkConfig proxyNetworkConfig;
    private ProxyCredentials proxyCredentials;
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();


    @BeforeEach
    void setUp(){
        proxyCredentials = new ProxyCredentials("username", "password");
        proxyNetworkConfig = new ProxyNetworkConfig("hostname", 8080);
        proxyConfigHolder = new ProxyConfigHolder(proxyNetworkConfig, proxyCredentials, 1L, true);
    }

    @Test
    @DisplayName("Test empty constructor with null proxyCredentials and  proxyNetworkConfig")
    void testEmptyConstructor(){
        ProxyConfigHolder configHolder = new ProxyConfigHolder();

        assertNull(configHolder.getProxyNetworkConfig());
        assertNull(configHolder.getProxyCredentials());
    }

    @Test
    @DisplayName("Test getting proxyCredentials from proxyConfigHolder object")
    void testProxyCredentialsGetHostname(){
        assertEquals( proxyCredentials, proxyConfigHolder.getProxyCredentials());
    }

    @Test
    @DisplayName("Test getting proxyConfigHolder from proxyConfigHolder object")
    void testProxyCredentialsGetPort(){
        assertEquals(proxyNetworkConfig, proxyConfigHolder.getProxyNetworkConfig());
    }

    @Test
    @DisplayName("Test setting proxyCredentials from proxyConfigHolder object")
    void testProxyConfigHolderSetProxyCredentials(){
        assertEquals(proxyCredentials, proxyConfigHolder.getProxyCredentials());
        proxyConfigHolder.setProxyCredentials(new ProxyCredentials( "username1","password1"));
        assertEquals("password1", proxyConfigHolder.getProxyCredentials().getPassword());
        assertEquals("username1", proxyConfigHolder.getProxyCredentials().getUsername());
    }

    @Test
    @DisplayName("Test setting proxyConfigHolder from proxyConfigHolder object")
    void testProxyConfigHolderSetProxyNetworkConfig(){
        assertEquals(proxyNetworkConfig, proxyConfigHolder.getProxyNetworkConfig());
        proxyConfigHolder.setProxyNetworkConfig(new ProxyNetworkConfig( "MYHOST",1234));
        assertEquals(1234, proxyConfigHolder.getProxyNetworkConfig().getPort());
        assertEquals("MYHOST", proxyConfigHolder.getProxyNetworkConfig().getHostname());
    }

    @Test
    @DisplayName("Test equality between ProxyNetworkConfig objects")
    void testEquals() {
        ProxyConfigHolder proxyConfigHolder1 = new ProxyConfigHolder();
        ProxyNetworkConfig config = new ProxyNetworkConfig();
        config.setHostname("hostname");
        config.setPort(8080);
        ProxyCredentials credentials = new ProxyCredentials();
        credentials.setUsername("username");
        credentials.setPassword("password");
        proxyConfigHolder1.setUseTimes(1L);
        proxyConfigHolder1.setUseAlways(true);

        proxyConfigHolder1.setProxyCredentials(credentials);
        proxyConfigHolder1.setProxyNetworkConfig(config);
        assertEquals(proxyConfigHolder, proxyConfigHolder1);
    }

    @Test
    @DisplayName("Test equality between proxyConfigHolder objects")
    void testEqualsFailed() {
        ProxyConfigHolder otherConfigHolder = getProxyConfigHolder("hostname2", 8082, "username2" , "password2", 1L, true);
        assertNotEquals(proxyConfigHolder, otherConfigHolder);
        ProxyConfigHolder otherConfigHolder1 = getProxyConfigHolder("hostname", 8080, "username" , "password3", 1L, true);
        assertNotEquals(proxyConfigHolder, otherConfigHolder1);
    }

    @Test
    @DisplayName("Test hashCode method for proxyConfigHolder object")
    void testHashCode() {
        ProxyConfigHolder proxyConfigHolder1 = getProxyConfigHolder("hostname", 8080, "username" , "password", 1L, true);
        ProxyConfigHolder proxyConfigHolder2 = getProxyConfigHolder("hostname", 8080, "username" , "password", 1L, true);

        assertEquals(proxyConfigHolder1, proxyConfigHolder2);
        assertEquals(proxyConfigHolder1.hashCode(), proxyConfigHolder2.hashCode());
    }


    private static ProxyConfigHolder getProxyConfigHolder(String hostname, Integer port, String username,String password,Long useTimes, boolean useAlways){
        return new ProxyConfigHolder(new ProxyNetworkConfig(hostname, port), new ProxyCredentials(username, password), useTimes, useAlways);
    }

    @Test
    @DisplayName("Test toString method for proxyConfigHolder object")
    void testToString() {
        String expected = "ProxyConfigHolder(proxyNetworkConfig=ProxyNetworkConfig(hostname=hostname, port=8080), proxyCredentials=ProxyCredentials(username=username, password=password), useTimes=1, useAlways=true)";
        assertEquals(expected, proxyConfigHolder.toString());
    }

    @Test
    @DisplayName("Test equality and hashCode for ProxyConfigHolder objects with the same values")
    void testEqualsAndHashCode() {
        ProxyConfigHolder proxyConfigHolder1 = new ProxyConfigHolder();
        ProxyNetworkConfig config = new ProxyNetworkConfig();
        config.setHostname("hostname");
        config.setPort(8080);
        ProxyCredentials credentials = new ProxyCredentials();
        credentials.setUsername("username");
        credentials.setPassword("password");
        proxyConfigHolder1.setUseAlways(true);
        proxyConfigHolder1.setUseTimes(1L);

        proxyConfigHolder1.setProxyCredentials(credentials);
        proxyConfigHolder1.setProxyNetworkConfig(config);

        assertEquals(proxyConfigHolder, proxyConfigHolder1);
        assertEquals(proxyConfigHolder.hashCode(), proxyConfigHolder1.hashCode());

    }

    @Test
    @DisplayName("Test validation fails for null proxyNetworkConfig")
    void testValidationFailsForNullProxyNetworkConfig() {
        ProxyConfigHolder configHolder = new ProxyConfigHolder(new ProxyNetworkConfig(null, null), proxyCredentials, 1L, true);

        Set<ConstraintViolation<ProxyConfigHolder>> violations = validator.validate(configHolder);
        assertEquals(3, violations.size());

        ConstraintViolation<ProxyConfigHolder> hostnameViolation = violations.stream()
                .filter(v -> "proxyNetworkConfig.hostname".equals(v.getPropertyPath().toString()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected violation for null hostname not found"));
        assertEquals("hostname can't be blank", hostnameViolation.getMessage());

        ConstraintViolation<ProxyConfigHolder> portViolation = violations.stream()
                .filter(v -> "proxyNetworkConfig.port".equals(v.getPropertyPath().toString()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected violation for null port not found"));
        assertEquals("port can't be null", portViolation.getMessage());

        ConstraintViolation<ProxyConfigHolder> thirdViolation = violations.stream()
                .filter(v -> "".equals(v.getPropertyPath().toString()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected violation for useTimes and useAlways property not found"));
        assertEquals("Specify only one of useTimes or useAlways", thirdViolation.getMessage());
    }

    @Test
    @DisplayName("Test validation fails for invalid proxyCredentials")
    void testValidationFailsForInvalidProxyCredentials() {
        ProxyConfigHolder configHolder = new ProxyConfigHolder(proxyNetworkConfig, new ProxyCredentials(null, null), 1L, false);

        Set<ConstraintViolation<ProxyConfigHolder>> violations = validator.validate(configHolder);
        assertEquals(2, violations.size());

        ConstraintViolation<ProxyConfigHolder> usernameViolation = violations.stream()
                .filter(v -> "proxyCredentials.username".equals(v.getPropertyPath().toString()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected violation for null username not found"));
        assertEquals("Username must not be blank", usernameViolation.getMessage());

        ConstraintViolation<ProxyConfigHolder> passwordViolation = violations.stream()
                .filter(v -> "proxyCredentials.password".equals(v.getPropertyPath().toString()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected violation for null password not found"));
        assertEquals("Password must not be blank", passwordViolation.getMessage());
    }

    private static Stream<Object[]> invalidProxyConfigHolder() {
        return Stream.of(
                new Object[] { new ProxyConfigHolder(null, new ProxyCredentials("username", "password"), 1L, true), "proxy network configuration cannot be null" },
                new Object[] { new ProxyConfigHolder(new ProxyNetworkConfig("hostname", 8080), new ProxyCredentials(null, "password"), 1L, true), "username cannot be null" },
                new Object[] { new ProxyConfigHolder(new ProxyNetworkConfig("hostname", 8080), new ProxyCredentials("username", ""), 1L, true), "password cannot be blank" }
        );
    }

    private static Stream<ProxyConfigHolder> validProxyConfigHolder() {
        return Stream.of(
                new ProxyConfigHolder(new ProxyNetworkConfig("hostname", 8080), new ProxyCredentials("username", "password"), 1L, true)
        );
    }
}
