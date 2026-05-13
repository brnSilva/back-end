package br.com.challenge.adapter.out.security;

import br.com.challenge.application.port.out.security.EncryptDataPort;
import br.com.challenge.config.properties.AesProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AesEncryptionAdapterTest {

    private EncryptDataPort encryptAdapter;

    @BeforeEach
    void setup() {
        AesProperties aesProperties = new AesProperties();
        aesProperties.setSecretKey("12345678901234567890123456789012");
        encryptAdapter = new AesEncryptionAdapter(aesProperties);
    }

    @Test
    void shouldEncryptAndReturnBase64String() {
        String input = "test data";
        String result = encryptAdapter.encrypt(input);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        assertTrue(result.matches("^[A-Za-z0-9+/]*={0,2}$"));
    }

    @Test
    void shouldReturnDifferentEncryptedValuesForDifferentInputs() {
        String encrypted1 = encryptAdapter.encrypt("input1");
        String encrypted2 = encryptAdapter.encrypt("input2");

        assertNotEquals(encrypted1, encrypted2);
    }

    @Test
    void shouldHandleEmptyString() {
        String result = encryptAdapter.encrypt("");

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}