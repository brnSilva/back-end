package br.com.challenge.adapter.out.security;

import br.com.challenge.application.port.out.security.HashDataPort;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Sha256HashAdapterTest {

    private final HashDataPort hashAdapter = new Sha256HashAdapter();

    @Test
    void shouldReturnNonEmptyString() {
        String result = hashAdapter.hash("test");

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void shouldReturnConsistentHashForSameInput() {
        String input = "consistent input";
        String hash1 = hashAdapter.hash(input);
        String hash2 = hashAdapter.hash(input);

        assertEquals(hash1, hash2);
    }

    @Test
    void shouldReturnDifferentHashesForDifferentInputs() {
        String hash1 = hashAdapter.hash("input1");
        String hash2 = hashAdapter.hash("input2");

        assertNotEquals(hash1, hash2);
    }
}