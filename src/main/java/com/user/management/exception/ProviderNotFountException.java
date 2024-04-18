package com.user.management.exception;

/**
 * provider를 찾을 수 없을 때 발생하는 예외 처리 class입니다.
 *
 * @author parksangwon
 * @version 1.0.0
 */
public class ProviderNotFountException extends RuntimeException {
    /**
     * Instantiates a new Provider not fount exception.
     */
    public ProviderNotFountException() {
        super("존재하지 않은 Provider 입니다.");
    }
}
