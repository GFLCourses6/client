package com.gfl.client.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@PropertySource("classpath:application-test.properties")
public class RsaManagerTest {

    @Autowired
    private RsaManager rsaManager;

    @Test
    void testEncryptionDecryption() {
        String originalMessage = "Hello, World!";
        String encryptedMessage = rsaManager.encrypt(originalMessage);
        String decryptedMessage = rsaManager.decrypt(encryptedMessage);
        assertEquals(originalMessage, decryptedMessage);
    }

    @Test
    void testEncryptionWithEmptyMessage() {
        String originalMessage = "";
        String encryptedMessage = rsaManager.encrypt(originalMessage);
        String decryptedMessage = rsaManager.decrypt(encryptedMessage);
        assertEquals(originalMessage, decryptedMessage);
    }

    @Test
    void testDecryptionWithEmptyMessage() {
        String encryptedMessage = "";
        String decryptedMessage = rsaManager.decrypt(encryptedMessage);
        assertEquals("", decryptedMessage);
    }

}