package com.user.management.exception;

/**
 * 사용자를 찾을 수 없는 경우에 대한 예외 처리 클래스입니다.
 * Author : jjunho50
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String id) {
        super(id + "는 존재하지 않는 userId 입니다.");
    }
}