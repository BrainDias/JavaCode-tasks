package org.oauthtask.services;

import org.oauthtask.loggers.CustomLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@Service
public class GitHubTokenRevocationService {

    private static final Logger logger = Logger.getLogger(GitHubTokenRevocationService.class.getName());

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String clientSecret;

    private final WebClient webClient;

    public GitHubTokenRevocationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.github.com")
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public void revokeAccessToken(String accessToken) {
        if (accessToken == null || accessToken.isBlank()) {
            return; // Если токен пустой или null, пропускаем отзыв
        }

        String url = "/applications/" + clientId + "/token";

        webClient.post()
                .uri(url)
                .headers(headers -> headers.setBasicAuth(clientId, clientSecret))
                .bodyValue("{\"access_token\":\"" + accessToken + "\"}")
                .retrieve();
    }
}
