package br.com.challenge.application.port.out.persistence;

import br.com.challenge.domain.model.entity.Card;

public interface SaveCardPort {
    Card save(Card card);
}
