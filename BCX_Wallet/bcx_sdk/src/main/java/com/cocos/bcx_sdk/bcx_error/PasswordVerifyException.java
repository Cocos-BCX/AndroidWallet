package com.cocos.bcx_sdk.bcx_error;

/**
 * net status exception
 */
public class PasswordVerifyException extends Exception {

    public PasswordVerifyException(String strMessage) {
        super(strMessage);
    }

    public PasswordVerifyException(Throwable throwable) {
        super(throwable);
    }
}
