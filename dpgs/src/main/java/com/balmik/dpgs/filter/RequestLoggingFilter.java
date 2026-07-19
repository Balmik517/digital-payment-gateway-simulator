package com.balmik.dpgs.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final String CORRELATION_ID="correlationId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String correlationId = UUID.randomUUID().toString();
        MDC.put(CORRELATION_ID, correlationId);
        response.setHeader("X-correlation-Id", correlationId);

        long startTime = System.currentTimeMillis();
        try{
            log.info("Incoming Request -> Method={} URI={}", request.getMethod(), request.getRequestURI());
            filterChain.doFilter(request, response);

        }finally {
            long duration = System.currentTimeMillis() - startTime;

            log.info("Completed Request -> Method={} URI={} Status={} Duration={}ms",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration);

            MDC.clear();

        }

    }
}
