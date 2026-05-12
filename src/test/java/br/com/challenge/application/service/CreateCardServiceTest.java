package br.com.challenge.application.service;

import br.com.challenge.application.dto.CreateCardCommand;
import br.com.challenge.application.port.out.persistence.SaveCardPort;
import br.com.challenge.application.port.out.security.EncryptDataPort;
import br.com.challenge.application.port.out.security.HashDataPort;
import br.com.challenge.domain.exception.CardAlreadyExistsException;
import br.com.challenge.domain.model.entity.Card;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCardServiceTest {

    @Mock
    private SaveCardPort saveCardPort;

    @Mock
    private HashDataPort hashDataPort;

    @Mock
    private EncryptDataPort encryptDataPort;

    @InjectMocks
    private CreateCardService createCardService;

    @Test
    void shouldCreateCardWhenHashDoesNotExist() {
        String cardNumber = "1234123412341234";
        String hash = "hash-value";
        String encrypted = "encrypted-value";
        Card expected = new Card(UUID.randomUUID(), encrypted, hash, LocalDateTime.now());

        when(hashDataPort.hash(cardNumber)).thenReturn(hash);
        when(saveCardPort.existsByHashCardNumber(hash)).thenReturn(false);
        when(encryptDataPort.encrypt(cardNumber)).thenReturn(encrypted);
        when(saveCardPort.save(any(Card.class))).thenReturn(expected);

        Card actual = createCardService.execute(new CreateCardCommand(cardNumber));

        assertSame(expected, actual);
        verify(hashDataPort).hash(cardNumber);
        verify(saveCardPort).existsByHashCardNumber(hash);
        verify(encryptDataPort).encrypt(cardNumber);
        verify(saveCardPort).save(any(Card.class));
    }

    @Test
    void shouldThrowWhenCardAlreadyExists() {
        String cardNumber = "1234123412341234";
        String hash = "hash-value";

        when(hashDataPort.hash(cardNumber)).thenReturn(hash);
        when(saveCardPort.existsByHashCardNumber(hash)).thenReturn(true);

        assertThrows(CardAlreadyExistsException.class,
                () -> createCardService.execute(new CreateCardCommand(cardNumber)));

        verify(saveCardPort).existsByHashCardNumber(hash);
        verify(saveCardPort, never()).save(any());
    }

    @Test
    void shouldThrowWhenSaveViolatesIntegrity() {
        String cardNumber = "1234123412341234";
        String hash = "hash-value";
        String encrypted = "encrypted-value";

        when(hashDataPort.hash(cardNumber)).thenReturn(hash);
        when(saveCardPort.existsByHashCardNumber(hash)).thenReturn(false);
        when(encryptDataPort.encrypt(cardNumber)).thenReturn(encrypted);
        when(saveCardPort.save(any(Card.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThrows(CardAlreadyExistsException.class,
                () -> createCardService.execute(new CreateCardCommand(cardNumber)));

        verify(saveCardPort).save(any(Card.class));
    }
}