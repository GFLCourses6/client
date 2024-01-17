package com.gfl.client.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.Set;
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
        proxyConfigHolder = new ProxyConfigHolder(proxyNetworkConfig, proxyCredentials);
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

        proxyConfigHolder1.setProxyCredentials(credentials);
        proxyConfigHolder1.setProxyNetworkConfig(config);
        assertEquals(proxyConfigHolder, proxyConfigHolder1);
    }

    @ParameterizedTest
    @MethodSource("getDifferentProxyConfigHolders")
    @DisplayName("Test equality between proxyConfigHolder objects")
    void testEqualsFailed(ProxyConfigHolder otherConfigHolder) {
        assertNotEquals(proxyConfigHolder, otherConfigHolder);
    }

    static Stream<ProxyConfigHolder> getDifferentProxyConfigHolders() {
        ProxyConfigHolder configHolder1 = new ProxyConfigHolder();
        ProxyNetworkConfig config1 = new ProxyNetworkConfig();
        config1.setHostname("hostname1");
        config1.setPort(8081);
        ProxyCredentials credentials1 = new ProxyCredentials();
        credentials1.setUsername("username1");
        credentials1.setPassword("password1");
        configHolder1.setProxyCredentials(credentials1);
        configHolder1.setProxyNetworkConfig(config1);

        ProxyConfigHolder configHolder2 = new ProxyConfigHolder();
        ProxyNetworkConfig config2 = new ProxyNetworkConfig();
        config2.setHostname("hostname2");
        config2.setPort(8082);
        ProxyCredentials credentials2 = new ProxyCredentials();
        credentials2.setUsername("username2");
        credentials2.setPassword("password2");
        configHolder2.setProxyCredentials(credentials2);
        configHolder2.setProxyNetworkConfig(config2);

        return Stream.of(configHolder1, configHolder2);
    }

    @Test
    @DisplayName("Test hashCode method for proxyConfigHolder object")
    void testHashCode() {
        int expected = proxyConfigHolder.hashCode();
        assertEquals(expected, proxyConfigHolder.hashCode());
    }

    @Test
    @DisplayName("Test toString method for proxyConfigHolder object")
    void testToString() {
        String expected = "ProxyConfigHolder(proxyNetworkConfig=" +
                "ProxyNetworkConfig(hostname=hostname, port=8080)," +
                " proxyCredentials=ProxyCredentials(username=username, password=password))";
        assertEquals(expected, proxyConfigHolder.toString());
    }

    @Test
    @DisplayName("Test equality and hashCode for   ProxyConfigHolder objects with the same values")
    void testEqualsAndHashCode() {
        ProxyConfigHolder proxyConfigHolder1 = new ProxyConfigHolder();
        ProxyNetworkConfig config = new ProxyNetworkConfig();
        config.setHostname("hostname");
        config.setPort(8080);
        ProxyCredentials credentials = new ProxyCredentials();
        credentials.setUsername("username");
        credentials.setPassword("password");

        proxyConfigHolder1.setProxyCredentials(credentials);
        proxyConfigHolder1.setProxyNetworkConfig(config);

        assertEquals(proxyConfigHolder, proxyConfigHolder1);
        assertEquals(proxyConfigHolder.hashCode(), proxyConfigHolder1.hashCode());

    }

    @Test
    @DisplayName("Test validation fails for null proxyNetworkConfig")
    void testValidationFailsForNullProxyNetworkConfig() {
        ProxyConfigHolder configHolder = new ProxyConfigHolder(null, proxyCredentials);

        Set<ConstraintViolation<ProxyConfigHolder>> violations = validator.validate(configHolder);

        // Assert violations for null proxyNetworkConfig
        assertEquals(1, violations.size());
        ConstraintViolation<ProxyConfigHolder> violation = violations.iterator().next();
        assertEquals("proxy network configuration can't be null", violation.getMessage());
        assertEquals("proxyNetworkConfig", violation.getPropertyPath().toString());
    }

    @Test
    @DisplayName("Test validation fails for invalid proxyCredentials")
    void testValidationFailsForInvalidProxyCredentials() {
        ProxyConfigHolder configHolder = new ProxyConfigHolder(proxyNetworkConfig, new ProxyCredentials(null, null)); // Assuming null values are invalid

        Set<ConstraintViolation<ProxyConfigHolder>> violations = validator.validate(configHolder);

        // Asserts the total number of violations -> Assert violations for invalid proxyCredentials
        assertEquals(2, violations.size()); // Two violations for null username and password

        // Verify violation for null username
        ConstraintViolation<ProxyConfigHolder> usernameViolation = violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals("proxyCredentials.username")) // Verifies specific violations for username and password using property paths.
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected violation for null username not found")); // Throws AssertionError if expected violations are not found.
        assertEquals("Username must not be blank", usernameViolation.getMessage());

        // Verify violation for null password
        ConstraintViolation<ProxyConfigHolder> passwordViolation = violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals("proxyCredentials.password"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected violation for null password not found"));
        assertEquals("Password must not be blank", passwordViolation.getMessage());
    }

}