package br.com.challenge.adapter.out.persistence.mapper;

import br.com.challenge.adapter.out.persistence.entity.CardEntity;
import br.com.challenge.domain.model.entity.Card;

public final class CardPersistenceMapper {
    
    private CardPersistenceMapper() {
    }

    public static Card toDomain(CardEntity cardEntity) {
        return new Card(
            cardEntity.getId(),
            cardEntity.getEncryptedCardNumber(),
            cardEntity.getHashCardNumber(),
            cardEntity.getCreatedAt()
        );
    }

    public static CardEntity toEntity(Card card) {
        return new CardEntity(
            card.encryptedCardNumber(),
            card.hashCardNumber(),
            card.createdAt()
        );
    }
}