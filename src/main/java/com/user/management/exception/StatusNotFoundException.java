package com.user.management.exception;

/**
 * 상태를 찾을 수 없을 때 발생하는 예외 클래스
 *
 * @author parksangwon
 * @version 1.0.0
 */
public class StatusNotFoundException extends RuntimeException {
    /**
     * 예외 기본 생성자
     */
    public StatusNotFoundException() {
        super();
    }

    /**
     * 메시지를 가지는 예외 생성자
     *
     * @param message 예외 메시지
     */
    public StatusNotFoundException(String message) {
        super(message);
    }
}
