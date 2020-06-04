package com.cocos.bcx_sdk.bcx_error;

public class AssetNotFoundException extends Exception {

    public AssetNotFoundException(String strMessage) {
        super(strMessage);
    }

    public AssetNotFoundException(Throwable throwable) {
        super(throwable);
    }

}