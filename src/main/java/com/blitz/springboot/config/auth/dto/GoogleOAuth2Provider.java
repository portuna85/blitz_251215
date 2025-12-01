package com.blitz.springboot.config.auth.dto;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Google OAuth2 Provider 구현체
 */
@Component
public class GoogleOAuth2Provider implements OAuth2Provider {

    private static final String GOOGLE = "google";

    @Override
    public OAuthAttributes extract(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    @Override
    public boolean supports(String registrationId) {
        return GOOGLE.equals(registrationId);
    }
}

