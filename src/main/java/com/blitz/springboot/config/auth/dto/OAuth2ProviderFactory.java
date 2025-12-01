package com.blitz.springboot.config.auth.dto;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * OAuth2 Provider 팩토리
 * Strategy Pattern을 통해 Provider별 처리 로직 분리
 */
@Component
public class OAuth2ProviderFactory {

    private final List<OAuth2Provider> providers;

    public OAuth2ProviderFactory(List<OAuth2Provider> providers) {
        this.providers = providers;
    }

    /**
     * registrationId에 해당하는 Provider를 찾아 사용자 정보 추출
     *
     * @param registrationId OAuth2 제공자 ID
     * @param userNameAttributeName 사용자 이름 속성명
     * @param attributes 사용자 속성
     * @return 추출된 사용자 정보
     * @throws IllegalArgumentException 지원하지 않는 Provider인 경우
     */
    public OAuthAttributes extract(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return providers.stream()
                .filter(provider -> provider.supports(registrationId))
                .findFirst()
                .map(provider -> provider.extract(userNameAttributeName, attributes))
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 OAuth2 Provider입니다: " + registrationId));
    }
}

