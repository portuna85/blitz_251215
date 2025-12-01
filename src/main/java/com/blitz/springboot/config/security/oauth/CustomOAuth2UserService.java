package com.blitz.springboot.config.security.oauth;

import com.blitz.springboot.config.security.oauth.dto.OAuthAttributes;
import com.blitz.springboot.config.security.oauth.provider.OAuth2ProviderFactory;
import com.blitz.springboot.config.security.oauth.dto.SessionUser;
import com.blitz.springboot.domain.user.User;
import com.blitz.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;
    private final OAuth2ProviderFactory providerFactory;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        log.debug("OAuth2 로그인 시도: provider={}, userNameAttribute={}", registrationId, userNameAttributeName);

        OAuthAttributes attributes = providerFactory.extract(
                registrationId,
                userNameAttributeName,
                oAuth2User.getAttributes()
        );

        User user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(user));

        log.info("OAuth2 로그인 성공: email={}, name={}", user.getEmail(), user.getName());

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.attributes(),
                attributes.nameAttributeKey()
        );
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.email())
                .map(entity -> entity.update(attributes.name(), attributes.picture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}

