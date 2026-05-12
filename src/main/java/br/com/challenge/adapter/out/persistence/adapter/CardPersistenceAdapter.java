package br.com.challenge.adapter.out.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.challenge.adapter.out.persistence.entity.CardEntity;
import br.com.challenge.adapter.out.persistence.mapper.CardPersistenceMapper;
import br.com.challenge.adapter.out.persistence.repository.CardRepository;
import br.com.challenge.application.port.out.persistence.FindCardPort;
import br.com.challenge.application.port.out.persistence.SaveCardPort;
import br.com.challenge.domain.model.entity.Card;
import br.com.challenge.domain.model.vo.CardNumber;

@Component
public class CardPersistenceAdapter implements SaveCardPort, FindCardPort {

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

    @Override
    public Optional<Card> findByCardNumber(CardNumber cardNumber) {
        return cardRepository
                .findByHashCardNumber(
                    cardNumber.value()
                ).map(
                    CardPersistenceMapper::toDomain
                );
    }
}