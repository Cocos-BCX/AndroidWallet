package com.cocos.bcx_sdk.bcx_error;

public class AccountNotFoundException extends Exception {

    public AccountNotFoundException(String strMessage) {
        super(strMessage);
    }

    public AccountNotFoundException(Throwable throwable) {
        super(throwable);
    }

}