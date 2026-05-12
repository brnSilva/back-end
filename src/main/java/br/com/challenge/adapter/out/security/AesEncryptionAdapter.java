package br.com.challenge.adapter.out.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import br.com.challenge.application.port.out.security.EncryptDataPort;
import br.com.challenge.config.properties.AesProperties;

@Component
public class AesEncryptionAdapter implements EncryptDataPort {

    private static final String ALGORITHM = "AES";
    private final AesProperties aesProperties;

    public AesEncryptionAdapter(AesProperties aesProperties) {
        this.aesProperties = aesProperties;
    }

    @Override
    public String encrypt(String value) {
        
        try {
            SecretKeySpec secretKeySpec =
                            new SecretKeySpec(
                                aesProperties.getSecretKey().getBytes(),
                                ALGORITHM
                            );
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            byte[] encryptedBytes = cipher.doFinal(
                                        value.getBytes(
                                            StandardCharsets.UTF_8
                                        )
                                    );
            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (Exception e) {

            throw new IllegalStateException(
                "Error encrypting data",
                e
            );
        }
    }
}