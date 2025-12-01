package com.blitz.springboot.config.security.oauth.provider;

import com.blitz.springboot.config.security.oauth.dto.OAuthAttributes;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class OAuth2ProviderFactory {

    private final List<OAuth2Provider> providers;

    public OAuth2ProviderFactory(List<OAuth2Provider> providers) {
        this.providers = providers;
    }

    public OAuthAttributes extract(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return providers.stream()
                .filter(provider -> provider.supports(registrationId))
                .findFirst()
                .map(provider -> provider.extract(userNameAttributeName, attributes))
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 OAuth2 Provider입니다: " + registrationId));
    }
}

