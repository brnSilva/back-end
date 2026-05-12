package br.com.challenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;

import com.nimbusds.jose.proc.SecurityContext;

import br.com.challenge.config.properties.JwtProperties;



@Configuration
public class JwtConfig {

    private final JwtProperties jwtProperties;

    public JwtConfig(
        JwtProperties jwtProperties
    ) {
        this.jwtProperties = jwtProperties;
    }

    public RSAKey generateRsa() {
        try {
            return new RSAKeyGenerator(2048)
                    .keyID(jwtProperties.getKeyId())
                    .generate();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = this.generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }
}