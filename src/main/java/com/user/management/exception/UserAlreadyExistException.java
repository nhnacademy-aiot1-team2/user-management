package com.user.management.exception;

public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException(String id)
    {
        super(id + "는 이미 존재하는 userId 입니다.");
    }
}
