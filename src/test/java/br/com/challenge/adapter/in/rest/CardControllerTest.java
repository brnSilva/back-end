package br.com.challenge.adapter.in.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.challenge.adapter.in.rest.request.CreateCardRequest;
import br.com.challenge.adapter.in.rest.response.UploadCardsResponse;
import br.com.challenge.application.port.in.CreateCardUseCase;
import br.com.challenge.application.port.in.FindCardUseCase;
import br.com.challenge.application.port.in.UploadCardsUseCase;
import br.com.challenge.domain.model.entity.Card;

@WebMvcTest(CardController.class)
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateCardUseCase createCardUseCase;

    @MockBean
    private FindCardUseCase findCardUseCase;

    @MockBean
    private UploadCardsUseCase uploadCardsUseCase;

    @Test
    @WithMockUser
    void shouldCreateCardSuccessfully() throws Exception {
        UUID cardId = UUID.randomUUID();
        Card card = new Card(cardId, "encrypted", "hash", LocalDateTime.now());
        CreateCardRequest request = new CreateCardRequest("1234123412341234");

        when(createCardUseCase.execute(any())).thenReturn(card);

        mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(cardId.toString()));
    }

    @Test
    @WithMockUser
    void shouldFindCardByNumber() throws Exception {
        String cardNumber = "1234123412341234";
        UUID cardId = UUID.randomUUID();

        when(findCardUseCase.execute(cardNumber)).thenReturn(cardId);

        mockMvc.perform(get("/cards/{cardNumber}", cardNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cardId.toString()));
    }

    @Test
    @WithMockUser
    void shouldUploadCardsFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "content".getBytes());
        UploadCardsResponse response = new UploadCardsResponse(null, null, true, true, 1, 1, 0, List.of());

        when(uploadCardsUseCase.execute(any())).thenReturn(response);

        mockMvc.perform(multipart("/cards/upload").file(file).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.processed").value(1))
                .andExpect(jsonPath("$.success").value(1))
                .andExpect(jsonPath("$.failed").value(0));
    }
}