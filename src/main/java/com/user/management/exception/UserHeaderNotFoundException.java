package com.user.management.exception;

/**
 * 사용자 헤더를 찾을 수 없는 경우에 대한 예외 처리 클래스입니다.
 * RuntimeException을 상속받아 만들어졌습니다.
 */
public class UserHeaderNotFoundException extends RuntimeException{

    /**
     * 기본 생성자에서는 "X-USER-ID header 값이 필요한 요청입니다."라는 메시지와 함께 예외를 발생시킵니다.
     * 이 클래스는 요청 헤더에 'X-USER-ID'가 존재하지 않을 때 사용됩니다.
     */
    public UserHeaderNotFoundException()
    {
        super("X-USER-ID header 값이 필요한 요청입니다.");
    }
}