package com.user.management.exception;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException()
    {
        super("패스워드가 올바르지 않습니다.");
    }
}
