package br.com.challenge.config;

import java.time.Duration;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;

import br.com.challenge.config.properties.JwtProperties;
import br.com.challenge.config.properties.SecurityProperties;

@Configuration
public class AuthorizationServerConfig {

    private final SecurityProperties properties;
    private final JwtProperties jwtProperties;

    public AuthorizationServerConfig(
        SecurityProperties properties,
        JwtProperties jwtProperties
    ) {
        this.properties = properties;
        this.jwtProperties = jwtProperties;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http
    ) throws Exception {

        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();

        http
            .securityMatcher(
                    authorizationServerConfigurer.getEndpointsMatcher()
            )

            .with(
                    authorizationServerConfigurer,
                    Customizer.withDefaults()
            )

            .authorizeHttpRequests(authorize ->
                    authorize.anyRequest().authenticated()
            );

        return http.build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {

        RegisteredClient registeredClient =
                RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId(properties.getClientId())
                        .clientSecret(
                                "{noop}" + properties.getClientSecret()
                        )
                        .authorizationGrantType(
                                AuthorizationGrantType.CLIENT_CREDENTIALS
                        )
                        .scopes(scopes ->
                                scopes.addAll(properties.getScopes())
                        )
                        .tokenSettings(
                                TokenSettings.builder()
                                        .accessTokenTimeToLive(
                                                Duration.ofSeconds(
                                                        jwtProperties.getExpiration()
                                                )
                                        ).build()
                        ).build();

        return new InMemoryRegisteredClientRepository(
                registeredClient
        );
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(jwtProperties.getIssuer())
                .build();
    }
}