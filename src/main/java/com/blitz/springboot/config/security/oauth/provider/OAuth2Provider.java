package com.blitz.springboot.config.security.oauth.provider;

import com.blitz.springboot.config.security.oauth.dto.OAuthAttributes;

import java.util.Map;

public interface OAuth2Provider {

    OAuthAttributes extract(String userNameAttributeName, Map<String, Object> attributes);

    boolean supports(String registrationId);
}

