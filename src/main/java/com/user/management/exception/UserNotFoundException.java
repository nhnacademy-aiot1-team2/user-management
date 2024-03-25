package com.user.management.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String id)
    {
        super(id + "는 존재하지 않는 userId 입니다.");
    }
}
