package com.cocos.bcx_sdk.bcx_error;

public class NotMemberException extends Exception {

    public NotMemberException(String strMessage) {
        super(strMessage);
    }

    public NotMemberException(Throwable throwable) {
        super(throwable);
    }

}