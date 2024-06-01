package com.user.management.exception;

/**
 * 권한을 찾을 수 없을 때 발생하는 예외 클래스
 *
 * @author parksangwon
 * @version 1.0.0
 */
public class RoleNotFoundException extends RuntimeException {

    /**
     * 예외 기본 생성자
     */
    public RoleNotFoundException() {
        super();
    }

    /**
     * 메시지를 가지는 예외 생성
     *
     * @param message 예외 메시
     */
    public RoleNotFoundException(String message) {
        super(message);
    }
}
