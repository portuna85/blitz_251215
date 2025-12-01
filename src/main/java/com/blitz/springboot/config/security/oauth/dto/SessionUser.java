package com.blitz.springboot.config.security.oauth.dto;

import com.blitz.springboot.domain.user.User;

import java.io.Serializable;

public record SessionUser(
        String name,
        String email,
        String picture
) implements Serializable {

    public SessionUser(User user) {
        this(
                user.getName(),
                user.getEmail(),
                user.getPicture()
        );
    }
}

