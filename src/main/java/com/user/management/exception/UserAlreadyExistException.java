package com.user.management.exception;

/**
 * 이미 존재하는 사용자에 대한 예외 처리 클래스입니다.
 * Author : jjunho50
 */
public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException(String id) {
        super(id + "는 이미 존재하는 userId 입니다.");
    }
}