package br.com.challenge.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String issuer;

    private Long expiration;

    private String keyId;

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) { this.issuer = issuer; }

    public Long getExpiration() { return expiration; }

    public void setExpiration(Long expiration) { this.expiration = expiration; }

    public String getKeyId() { return keyId; }

    public void setKeyId(String keyId) { this.keyId = keyId; }
}