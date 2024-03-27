package com.user.management.exception;

/**
 * 사용자를 찾을 수 없는 경우에 대한 예외 처리 클래스입니다.
 * RuntimeException을 상속받아 만들어졌습니다.
 */
public class UserNotFoundException extends RuntimeException{

    /**
     * 생성자에서는 사용자 ID를 인자로 받아, 해당 사용자 ID가 존재하지 않을 경우
     * "[사용자ID]는 존재하지 않는 userId 입니다."라는 메시지와 함께 예외를 발생시킵니다.
     * 이 클래스는 존재하지 않는 사용자 ID에 대한 요청이 있을 때 사용됩니다.
     * @param id 입력한 사용자 ID
     */
    public UserNotFoundException(String id)
    {
        super(id + "는 존재하지 않는 userId 입니다.");
    }
}