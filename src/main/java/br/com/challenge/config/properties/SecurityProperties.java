package br.com.challenge.config.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.oauth2")
public class SecurityProperties {

    private String clientId;

    private String clientSecret;

    private List<String> scopes;

    public String getClientId() { return clientId;}

    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getClientSecret() { return clientSecret; }

    public void setClientSecret(String clientSecret) { this.clientSecret = clientSecret; }

    public List<String> getScopes() { return scopes; }

    public void setScopes(List<String> scopes) { this.scopes = scopes; }
}