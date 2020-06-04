package com.cocos.bcx_sdk.bcx_error;

/**
 * net status exception
 */
public class AuthorityException extends Exception {

    public AuthorityException(String strMessage) {
        super(strMessage);
    }

    public AuthorityException(Throwable throwable) {
        super(throwable);
    }
}
