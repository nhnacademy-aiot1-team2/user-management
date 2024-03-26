package com.user.management.exception;

public class OnlyAdminCanAccessUserDataException extends RuntimeException{
    public OnlyAdminCanAccessUserDataException()
    {
        super("관리자 권한이 필요한 요청입니다.");
    }
}
