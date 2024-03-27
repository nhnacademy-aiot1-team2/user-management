package com.user.management.exception;

/**
 * 유효하지 않은 비밀번호에 대한 예외 처리 클래스입니다.
 * RuntimeException을 상속 받아서 만들어졌습니다.
 */
public class InvalidPasswordException extends RuntimeException{

    /**
     * 기본 생성자에서는 "패스워드가 올바르지 않습니다."라는 메시지와 함께 예외를 발생시킵니다.
     * 이 클래스는 비밀번호가 유효하지 않을 경우에 사용됩니다.
     */
    public InvalidPasswordException()
    {
        super("패스워드가 올바르지 않습니다.");
    }
}