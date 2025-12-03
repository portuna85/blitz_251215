package com.blitz.springboot.common.exception;

/**
 * 권한 없음 예외
 * 사용자가 리소스에 접근할 권한이 없을 때 발생
 */
public class UnauthorizedException extends BusinessException {

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UnauthorizedException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}

