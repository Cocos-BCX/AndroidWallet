package com.cocos.bcx_sdk.bcx_error;

public class CreateAccountException extends Exception {

    public CreateAccountException(String strMessage) {
        super(strMessage);
    }

    public CreateAccountException(Throwable throwable) {
        super(throwable);
    }

}