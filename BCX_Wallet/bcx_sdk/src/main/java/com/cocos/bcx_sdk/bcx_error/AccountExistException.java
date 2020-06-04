package com.cocos.bcx_sdk.bcx_error;

public class AccountExistException extends Exception {

    public AccountExistException(String strMessage) {
        super(strMessage);
    }

    public AccountExistException(Throwable throwable) {
        super(throwable);
    }

}