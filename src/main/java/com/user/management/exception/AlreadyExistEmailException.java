package com.user.management.exception;

public class AlreadyExistEmailException extends RuntimeException{
    public AlreadyExistEmailException(String email)
    {
        super(email + "은 이미 존재하는 이메일입니다.");
    }
}
