package br.com.challenge.config;

import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;

public final class JwtConfig {

    private JwtConfig() {}

    public static RSAKey generateRsa() {
        try {
            return new RSAKeyGenerator(2048)
                    .keyID("challenge-key")
                    .generate();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}