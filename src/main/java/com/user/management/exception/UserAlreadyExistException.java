package com.user.management.exception;

/**
 * 이미 존재하는 사용자에 대한 예외 처리 클래스입니다.
 * RuntimeException을 상속받아 만들어졌습니다.
 */
public class UserAlreadyExistException extends RuntimeException{

    /**
     * 생성자에서는 사용자 ID를 인자로 받아, 해당 사용자 ID가 이미 존재할 경우
     * "[사용자ID]는 이미 존재하는 userId 입니다."라는 메시지와 함께 예외를 발생시킵니다.
     * 이 클래스는 새로운 사용자 생성 시 이미 존재하는 사용자 ID를 입력했을 때 사용됩니다.
     * @param id 입력한 사용자 ID
     */
    public UserAlreadyExistException(String id)
    {
        super(id + "는 이미 존재하는 userId 입니다.");
    }
}