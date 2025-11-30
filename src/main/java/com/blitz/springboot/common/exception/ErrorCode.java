package com.blitz.springboot.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_INPUT_VALUE(400, "C001", "잘못된 입력값입니다."),
    INTERNAL_SERVER_ERROR(500, "C002", "서버 오류가 발생했습니다."),

    POST_NOT_FOUND(404, "P001", "게시글을 찾을 수 없습니다."),

    USER_NOT_FOUND(404, "U001", "사용자를 찾을 수 없습니다."),

    OAUTH_PROVIDER_NOT_SUPPORTED(400, "O001", "지원하지 않는 OAuth 제공자입니다.");

    private final int status;
    private final String code;
    private final String message;
}

