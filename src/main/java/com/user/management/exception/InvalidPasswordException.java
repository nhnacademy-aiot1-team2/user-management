package com.user.management.exception;

/**
 * 유효하지 않은 비밀번호에 대한 예외 처리 클래스입니다.
 * Author : jjunho50
 */
public class InvalidPasswordException extends RuntimeException{

    public InvalidPasswordException()
    {
        super("패스워드가 올바르지 않습니다.");
    }
}