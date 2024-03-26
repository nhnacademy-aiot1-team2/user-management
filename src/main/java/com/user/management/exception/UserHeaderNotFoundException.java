package com.user.management.exception;

public class UserHeaderNotFoundException extends RuntimeException{
    public UserHeaderNotFoundException()
    {
        super("X-USER-ID header 값이 필요한 요청입니다.");
    }
}
