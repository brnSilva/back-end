package br.com.challenge.application.service;

import br.com.challenge.application.port.out.persistence.FindCardPort;
import br.com.challenge.application.port.out.security.HashDataPort;
import br.com.challenge.domain.exception.CardNotFoundException;
import br.com.challenge.domain.model.entity.Card;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindCardServiceTest {

    @Mock
    private FindCardPort findCardPort;

    @Mock
    private HashDataPort hashDataPort;

    @InjectMocks
    private FindCardService findCardService;

    @Test
    void shouldReturnCardIdWhenCardExists() {
        String cardNumber = "1234123412341234";
        String hash = "hash-value";
        UUID expectedId = UUID.randomUUID();
        Card card = new Card(expectedId, "encrypted", hash, LocalDateTime.now());

        when(hashDataPort.hash(cardNumber)).thenReturn(hash);
        when(findCardPort.findByHashCardNumber(hash)).thenReturn(Optional.of(card));

        UUID actual = findCardService.execute(cardNumber);

        assertEquals(expectedId, actual);
        verify(hashDataPort).hash(cardNumber);
        verify(findCardPort).findByHashCardNumber(hash);
    }

    @Test
    void shouldThrowWhenCardDoesNotExist() {
        String cardNumber = "1234123412341234";
        String hash = "hash-value";

        when(hashDataPort.hash(cardNumber)).thenReturn(hash);
        when(findCardPort.findByHashCardNumber(hash)).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class,
                () -> findCardService.execute(cardNumber));

        verify(hashDataPort).hash(cardNumber);
        verify(findCardPort).findByHashCardNumber(hash);
    }
}