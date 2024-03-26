package com.user.management.exception;

public class UserOnlyUpdateOwnData extends RuntimeException{
    public UserOnlyUpdateOwnData()
    {
        super("본인의 데이터만 수정할 수 있습니다.");
    }
}
