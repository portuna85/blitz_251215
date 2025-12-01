package com.blitz.springboot.config.auth.dto;

import com.blitz.springboot.domain.user.Role;
import com.blitz.springboot.domain.user.User;
import lombok.Builder;

import java.util.Map;

/**
 * OAuth2 인증 사용자 정보를 담는 DTO
 * Record 타입으로 불변성 보장
 */
public record OAuthAttributes(
        Map<String, Object> attributes,
        String nameAttributeKey,
        String name,
        String email,
        String picture) {

    @Builder
    public OAuthAttributes {
        validateName(name);
        validateEmail(email);
    }

    /**
     * OAuth2 인증 정보를 User 엔티티로 변환
     *
     * @return User 엔티티
     */
    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.USER)
                .build();
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("OAuth2 사용자 이름은 필수입니다");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("OAuth2 사용자 이메일은 필수입니다");
        }
    }
}
