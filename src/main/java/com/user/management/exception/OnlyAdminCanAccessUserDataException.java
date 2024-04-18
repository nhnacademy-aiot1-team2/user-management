package com.user.management.exception;

/**
 * 관리자만이 접근할 수 있는 데이터에 대한 예외 처리 클래스입니다.
 *
 * @author jjunho50
 * @version 1.0.0
 */
public class OnlyAdminCanAccessUserDataException extends RuntimeException {

    public OnlyAdminCanAccessUserDataException() {
        super("관리자 권한이 필요한 요청입니다.");
    }
}