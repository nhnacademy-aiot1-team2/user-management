package com.user.management.exception;

/**
 * 관리자만이 접근할 수 있는 데이터에 대한 예외 처리 클래스입니다.
 * RuntimeException을 상속받아 만들어졌습니다.
 */
public class OnlyAdminCanAccessUserDataException extends RuntimeException{

    /**
     * 기본 생성자에서는 "관리자 권한이 필요한 요청입니다."라는 메시지와 함께 예외를 발생시킵니다.
     * 이 클래스는 관리자만이 접근 가능한 데이터에 다른 사용자가 접근하려 할 시에 사용됩니다.
     */
    public OnlyAdminCanAccessUserDataException()
    {
        super("관리자 권한이 필요한 요청입니다.");
    }
}