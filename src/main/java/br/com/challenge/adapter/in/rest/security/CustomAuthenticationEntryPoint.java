package br.com.challenge.adapter.in.rest.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.challenge.adapter.in.rest.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest request, 
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {

        String message = "Unauthorized access";

        if(authException.getMessage() != null
            && authException.getMessage().toLowerCase().contains("expired")) {
            message = "Token expired";
        }

        LOGGER.warn( "Unauthorized access - method={}, path={}, ip={}, error={}",
                                                request.getMethod(),
                                                request.getRequestURI(),
                                                request.getRemoteAddr(),
                                                authException.getMessage() );

        ErrorResponse errorResponse = new ErrorResponse(
                                        HttpStatus.UNAUTHORIZED.value(),
                                        List.of(message),
                                        LocalDateTime.now());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        
        objectMapper.writeValue(
                        response.getOutputStream(),
                        errorResponse
        );
    }
}