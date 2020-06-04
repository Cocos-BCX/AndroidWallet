package com.cocos.bcx_sdk.bcx_error;

/**
 * net status exception
 */
public class LockOrUnLoginException extends Exception {

    public LockOrUnLoginException(String strMessage) {
        super(strMessage);
    }

    public LockOrUnLoginException(Throwable throwable) {
        super(throwable);
    }
}
