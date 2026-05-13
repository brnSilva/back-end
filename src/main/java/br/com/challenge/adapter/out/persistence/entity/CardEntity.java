package br.com.challenge.adapter.out.persistence.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cards")
public class CardEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String encryptedCardNumber;

    @Column(nullable = false, unique = true)
    private String hashCardNumber;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected CardEntity() {
    }

    public CardEntity(
            String encryptedCardNumber,
            String hashCardNumber,
            LocalDateTime createdAt
    ) {
        this.encryptedCardNumber = encryptedCardNumber;
        this.hashCardNumber = hashCardNumber;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getEncryptedCardNumber() {
        return encryptedCardNumber;
    }

    public String getHashCardNumber() {
        return hashCardNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
