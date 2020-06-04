package com.cocos.bcx_sdk.bcx_error;

/**
 * @author ningkang.guo
 * @Date 2019/6/3
 */
public class WordViewNotExistException extends Exception {
    public WordViewNotExistException(String strMessage) {
        super(strMessage);
    }

    public WordViewNotExistException(Throwable throwable) {
        super(throwable);
    }
}
