package com.blitz.springboot.config.security.oauth.provider;

import com.blitz.springboot.config.security.oauth.dto.OAuthAttributes;
import org.springframework.stereotype.Component;

import java.util.Map;

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

