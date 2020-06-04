package com.cocos.bcx_sdk.bcx_error;

public class OrderNotFoundException extends Exception {

    public OrderNotFoundException(String strMessage) {
        super(strMessage);
    }

    public OrderNotFoundException(Throwable throwable) {
        super(throwable);
    }

}