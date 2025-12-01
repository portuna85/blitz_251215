package com.blitz.springboot.config.security.oauth.dto;

import com.blitz.springboot.domain.user.Role;
import com.blitz.springboot.domain.user.User;
import lombok.Builder;

import java.util.Map;

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

