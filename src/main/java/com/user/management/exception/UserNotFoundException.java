package com.user.management.exception;

/**
 * 사용자를 찾을 수 없는 경우에 대한 예외 처리 클래스입니다.
 *
 * @author jjunho50
 * @version 1.0.0
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * 예외 발생 아이디를 가지는 예외 생성자
     *
     * @param id 예외 발생 아이디
     */
    public UserNotFoundException(String id) {
        super(id + "는 존재하지 않는 userId 입니다.");
    }
}