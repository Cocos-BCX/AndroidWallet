package com.cocos.bcx_sdk.bcx_error;

public class NhAssetNotFoundException extends Exception {

    public NhAssetNotFoundException(String strMessage) {
        super(strMessage);
    }

    public NhAssetNotFoundException(Throwable throwable) {
        super(throwable);
    }

}