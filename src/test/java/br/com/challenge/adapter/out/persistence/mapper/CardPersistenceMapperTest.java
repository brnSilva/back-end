package br.com.challenge.adapter.out.persistence.mapper;

import br.com.challenge.adapter.out.persistence.entity.CardEntity;
import br.com.challenge.domain.model.entity.Card;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CardPersistenceMapperTest {

    @Test
    void shouldMapEntityToDomain() throws Exception {
        UUID id = UUID.randomUUID();
        String encrypted = "encrypted";
        String hash = "hash";
        LocalDateTime createdAt = LocalDateTime.now();

        CardEntity entity = new CardEntity(encrypted, hash, createdAt);

        Field idField = CardEntity.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(entity, id);

        Card domain = CardPersistenceMapper.toDomain(entity);

        assertEquals(id, domain.id());
        assertEquals(encrypted, domain.encryptedCardNumber());
        assertEquals(hash, domain.hashCardNumber());
        assertEquals(createdAt, domain.createdAt());
    }

    @Test
    void shouldMapDomainToEntity() {
        UUID id = UUID.randomUUID();
        String encrypted = "encrypted";
        String hash = "hash";
        LocalDateTime createdAt = LocalDateTime.now();

        Card domain = new Card(id, encrypted, hash, createdAt);

        CardEntity entity = CardPersistenceMapper.toEntity(domain);

        assertNull(entity.getId());
        assertEquals(encrypted, entity.getEncryptedCardNumber());
        assertEquals(hash, entity.getHashCardNumber());
        assertEquals(createdAt, entity.getCreatedAt());
    }
}