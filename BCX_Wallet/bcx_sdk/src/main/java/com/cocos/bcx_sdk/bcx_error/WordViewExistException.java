package com.cocos.bcx_sdk.bcx_error;

/**
 * @author ningkang.guo
 * @Date 2019/9/25
 */
public class WordViewExistException extends Throwable {
    public WordViewExistException(String strMessage) {
        super(strMessage);
    }

    public WordViewExistException(Throwable throwable) {
        super(throwable);
    }
}
