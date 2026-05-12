package br.com.challenge.adapter.out.persistence.adapter;

import org.springframework.stereotype.Component;

import br.com.challenge.adapter.out.persistence.entity.CardEntity;
import br.com.challenge.adapter.out.persistence.mapper.CardPersistenceMapper;
import br.com.challenge.adapter.out.persistence.repository.CardRepository;
import br.com.challenge.application.port.out.persistence.SaveCardPort;
import br.com.challenge.domain.model.entity.Card;

@Component
public class CardPersistenceAdapter implements SaveCardPort {

    private final CardRepository cardRepository;

    public CardPersistenceAdapter(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }
    
    @Override
    public Card save(Card card) {
        CardEntity entity = CardPersistenceMapper.toEntity(card);

        CardEntity savedEntity = cardRepository.save(entity);

        return CardPersistenceMapper.toDomain(savedEntity);
    }
    
}
