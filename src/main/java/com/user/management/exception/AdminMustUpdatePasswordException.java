package com.user.management.exception;


/**
 * 관리자가 첫 로그인 시 비밀번호를 변경하지 않았을 때 발생하는 예외 클래스 입니다
 *
 * @author jjunho50
 * @version 1.0.0
 */
public class AdminMustUpdatePasswordException extends RuntimeException {
    public AdminMustUpdatePasswordException() {
        super("어드민은 첫 로그인 시, 반드시 초기 비밀번호를 변경해야 합니다!!!");
    }
}
