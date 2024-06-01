package com.user.management.exception;

/**
 * 사용자가 자신의 데이터만 수정할 수 있음을 나타내는 예외 처리 클래스입니다.
 *
 * @author jjunho50
 * @version 1.0.0
 */
public class UserOnlyUpdateOwnDataException extends RuntimeException {
    /**
     * 예외 생성자
     */
    public UserOnlyUpdateOwnDataException() {
        super("본인의 데이터만 수정할 수 있습니다.");
    }
}