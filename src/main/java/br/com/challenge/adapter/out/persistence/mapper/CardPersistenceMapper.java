package br.com.challenge.adapter.out.persistence.mapper;

import br.com.challenge.adapter.out.persistence.entity.CardEntity;
import br.com.challenge.domain.model.entity.Card;
import br.com.challenge.domain.model.vo.CardNumber;

public final class CardPersistenceMapper {
    
    private CardPersistenceMapper() {
    }

    public static Card toDomain(CardEntity cardEntity) {
        return new Card(
            cardEntity.getId(),
           // cardEntity.getEncryptedCardNumber(),
            new CardNumber(cardEntity.getHashCardNumber()),
            cardEntity.getCreatedAt()
        );
    }

    public static CardEntity toEntity(Card card) {
        return new CardEntity(
            //card.encryptedCardNumber(),
            card.cardNumber().value(),
            card.cardNumber().value(),
            card.createdAt()
        );
    }
}
