package com.user.management.exception;

public class ProviderNotFountException extends RuntimeException{
    public ProviderNotFountException()
    {
        super("존재하지 않은 Provider 입니다.");
    }
}
