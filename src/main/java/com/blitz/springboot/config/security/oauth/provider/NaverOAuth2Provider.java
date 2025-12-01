package com.blitz.springboot.config.security.oauth.provider;

import com.blitz.springboot.config.security.oauth.dto.OAuthAttributes;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NaverOAuth2Provider implements OAuth2Provider {

    private static final String NAVER = "naver";
    private static final String RESPONSE_KEY = "response";

    @Override
    public OAuthAttributes extract(String userNameAttributeName, Map<String, Object> attributes) {
        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) attributes.get(RESPONSE_KEY);

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    @Override
    public boolean supports(String registrationId) {
        return NAVER.equals(registrationId);
    }
}

