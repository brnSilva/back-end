package br.com.challenge.adapter.out.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.challenge.adapter.out.persistence.entity.CardEntity;

public interface CardRepository extends JpaRepository<CardEntity, UUID> {
    
    Optional<CardEntity> findByHashCardNumber(String hashCardNumber);
}
