package com.cocos.bcx_sdk.bcx_error;

public class PasswordInvalidException extends Throwable {

    public PasswordInvalidException(String strMessage) {
        super(strMessage);
    }

    public PasswordInvalidException(Throwable throwable) {
        super(throwable);
    }
}
