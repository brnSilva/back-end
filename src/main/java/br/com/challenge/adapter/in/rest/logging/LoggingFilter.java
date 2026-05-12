package br.com.challenge.adapter.in.rest.logging;


import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

    private static final String CORRELATION_ID = "correlationId";

    @Override
    protected void doFilterInternal(
                        HttpServletRequest request,
                        HttpServletResponse response,
                        FilterChain filterChain
    ) throws IOException, ServletException {
        
        String correlationId = UUID.randomUUID().toString();

        MDC.put(CORRELATION_ID, correlationId);

        long start = System.currentTimeMillis();

        try {
            
            filterChain.doFilter(request, response);


        } finally {
            long duration = System.currentTimeMillis() - start;

            LOGGER.info("[{}] {} {} - status={} - duration={} ms",
                        correlationId,
                        request.getMethod(),
                        request.getRequestURI(),
                        response.getStatus(),
                        duration);
        }

        MDC.clear();
    }
}