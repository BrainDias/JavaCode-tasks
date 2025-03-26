package org.oauthtask.loggers;

import java.util.logging.Logger;

import org.oauthtask.services.GitHubTokenRevocationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    private static final Logger logger = Logger.getLogger(CustomLogoutSuccessHandler.class.getName());
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final GitHubTokenRevocationService revocationService;

    public CustomLogoutSuccessHandler(OAuth2AuthorizedClientService authorizedClientService,
                                      GitHubTokenRevocationService revocationService) {
        this.authorizedClientService = authorizedClientService;
        this.revocationService = revocationService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException {
        if (authentication != null) {
            String principalName = authentication.getName();
            OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient("github", principalName);

            if (client != null) {
                String accessToken = client.getAccessToken().getTokenValue();
                revocationService.revokeAccessToken(accessToken);
                authorizedClientService.removeAuthorizedClient("github", principalName);
            }

            logger.info("User '" + principalName + "' logged out and token revoked");
        }

        response.sendRedirect("/login?logout");
    }
}


