package org.jwttask.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LoggingFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String authHeader = request.getHeader("Authorization");
        logger.info("Request: {} {}", method, uri);

        if (authHeader != null) {
            logger.info("Authorization: Bearer ***");
        }

        filterChain.doFilter(request, response);
        logger.info("Response: {} {}", response.getStatus(), response.getContentType());
    }
}

