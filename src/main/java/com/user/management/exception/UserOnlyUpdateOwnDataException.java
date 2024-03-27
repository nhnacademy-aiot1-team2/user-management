package com.user.management.exception;

/**
 * 사용자가 자신의 데이터만 수정할 수 있음을 나타내는 예외 처리 클래스입니다.
 * RuntimeException을 상속받아 만들어졌습니다.
 */
public class UserOnlyUpdateOwnDataException extends RuntimeException{

    /**
     * 기본 생성자에서는 "본인의 데이터만 수정할 수 있습니다."라는 메시지와 함께 예외를 발생시킵니다.
     * 이 클래스는 사용자가 다른 사용자의 데이터를 수정하려고 할 때 사용됩니다.
     */
    public UserOnlyUpdateOwnDataException()
    {
        super("본인의 데이터만 수정할 수 있습니다.");
    }
}