package com.blitz.springboot.config.auth.dto;

import java.util.Map;

/**
 * OAuth2 Provider별 사용자 정보 추출 전략 인터페이스
 * OCP(개방-폐쇄 원칙) 준수: 새로운 Provider 추가 시 기존 코드 수정 없이 확장 가능
 */
public interface OAuth2Provider {

    /**
     * Provider에서 사용자 정보를 추출하여 OAuthAttributes 생성
     *
     * @param userNameAttributeName OAuth2 제공자의 사용자 이름 속성명
     * @param attributes OAuth2 제공자로부터 받은 사용자 속성
     * @return 추출된 사용자 정보를 담은 OAuthAttributes
     */
    OAuthAttributes extract(String userNameAttributeName, Map<String, Object> attributes);

    /**
     * 해당 Provider가 처리 가능한지 확인
     *
     * @param registrationId OAuth2 제공자 ID
     * @return 처리 가능 여부
     */
    boolean supports(String registrationId);
}

