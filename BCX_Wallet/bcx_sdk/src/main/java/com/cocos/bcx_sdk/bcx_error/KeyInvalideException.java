package com.cocos.bcx_sdk.bcx_error;

public class KeyInvalideException extends Exception {

    public KeyInvalideException(String strMessage) {
        super(strMessage);
    }

    public KeyInvalideException(Throwable throwable) {
        super(throwable);
    }

}