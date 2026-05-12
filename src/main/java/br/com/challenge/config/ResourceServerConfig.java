package br.com.challenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import br.com.challenge.adapter.in.rest.security.CustomAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class ResourceServerConfig {

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public ResourceServerConfig(CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
            .securityMatcher("/**")
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                        "/h2-console/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**"
                    ).permitAll()
                    .anyRequest().authenticated()
            )

            .oauth2ResourceServer(resource ->
                    resource.jwt(Customizer.withDefaults())
            )
            .exceptionHandling(exception ->
                exception.authenticationEntryPoint(
                        customAuthenticationEntryPoint
                )
            );

        http.headers(headers ->
                headers.frameOptions(frame -> frame.disable())
        );

        return http.build();
    }
}