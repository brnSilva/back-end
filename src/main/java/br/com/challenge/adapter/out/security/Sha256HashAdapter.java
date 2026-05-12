package br.com.challenge.adapter.out.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

import br.com.challenge.application.port.out.security.HashDataPort;

@Component
public class Sha256HashAdapter implements HashDataPort {

    @Override
    public String hash(String value) {
        try {
            MessageDigest digest = 
                MessageDigest.getInstance("SHA-256");

            byte[] hashBytes =
                    digest.digest(
                        value.getBytes(
                            StandardCharsets.UTF_8
                        ));
            
            StringBuilder hexString =
                    new StringBuilder();

            for (byte b : hashBytes) {
                hexString.append(
                    String.format("%02x", b)
                );
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                "Error generating SHA-256 hash",
                e
            );
        }
    }
}