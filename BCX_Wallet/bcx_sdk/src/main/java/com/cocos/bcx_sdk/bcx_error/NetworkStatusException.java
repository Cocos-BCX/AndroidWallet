package com.cocos.bcx_sdk.bcx_error;

/**
 * net status exception
 */
public class NetworkStatusException extends Exception {

    public NetworkStatusException(String strMessage) {
        super(strMessage);
    }

    public NetworkStatusException(Throwable throwable) {
        super(throwable);
    }
}
