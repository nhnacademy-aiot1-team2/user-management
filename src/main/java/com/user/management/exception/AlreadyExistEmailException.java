package com.user.management.exception;

/**
 * 이미 등록된 이메일로 회원가입 하려 할 시, 발생하는 예외 클래스입니다.
 * Author : jjunho50
 */
public class AlreadyExistEmailException extends RuntimeException {
    public AlreadyExistEmailException(String email) {
        super(email + "은 이미 존재하는 이메일입니다.");
    }
}
