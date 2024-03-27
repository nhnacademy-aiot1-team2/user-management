package com.user.management.exception;

public class AdminMustUpdatePasswordException extends RuntimeException{
    public AdminMustUpdatePasswordException()
    {
        super("어드민은 첫 로그인 시, 반드시 초기 비밀번호를 변경해야 합니다!!!");
    }
}
