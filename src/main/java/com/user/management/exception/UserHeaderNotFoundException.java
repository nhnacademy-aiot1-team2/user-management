package com.user.management.exception;

/**
 * 사용자 헤더를 찾을 수 없는 경우에 대한 예외 처리 클래스입니다.
 * Author : jjunho50
 */
public class UserHeaderNotFoundException extends RuntimeException {

    public UserHeaderNotFoundException() {
        super("X-USER-ID header 값이 필요한 요청입니다.");
    }
}