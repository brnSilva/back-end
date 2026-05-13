package br.com.challenge.application.service;

import br.com.challenge.adapter.in.rest.response.UploadCardsResponse;
import br.com.challenge.application.port.in.CreateCardUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadCardsServiceTest {

    @Mock
    private CreateCardUseCase createCardUseCase;

    @InjectMocks
    private UploadCardsService uploadCardsService;

    private MultipartFile validFile;
    private MultipartFile mixedFile;

    @BeforeEach
    void setup() {
        validFile = new MockMultipartFile(
                "file",
                "cards.csv",
                "text/csv",
                "header\nC0000011234123412341234\nLOTE".getBytes(StandardCharsets.UTF_8)
        );

        mixedFile = new MockMultipartFile(
                "file",
                "cards.csv",
                "text/csv",
                "header\nC0000011234123412341234\nC0000020000000000000000\nINVALID\nLOTE".getBytes(StandardCharsets.UTF_8)
        );
    }

    @Test
    void shouldReturnSuccessWhenAllValidCardLinesAreProcessed() {
        UploadCardsResponse response = uploadCardsService.execute(validFile);

        assertEquals(1, response.processed());
        assertEquals(1, response.success());
        assertEquals(0, response.failed());
        assertTrue(response.errorDetails().isEmpty());

        verify(createCardUseCase).execute(
            argThat(command -> command.cardNumber().equals("1234123412341234")));
    }

    @Test
    void shouldCollectErrorsWhenCreateCardThrows() {
        doThrow(new IllegalArgumentException("duplicate"))
                .when(createCardUseCase).execute(
                    argThat(command -> command.cardNumber().equals("1234123412341234")));

        UploadCardsResponse response = uploadCardsService.execute(mixedFile);

        assertEquals(2, response.processed());
        assertEquals(1, response.success());
        assertEquals(1, response.failed());
        assertEquals(1, response.errorDetails().size());

        assertEquals( "C000001", response.errorDetails().get(0).cardIdentifier() );

        assertTrue(response.errorDetails().stream().allMatch(detail -> detail.message().contains("duplicate")));
    }
}