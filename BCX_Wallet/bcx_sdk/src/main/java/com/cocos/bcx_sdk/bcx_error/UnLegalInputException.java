package com.cocos.bcx_sdk.bcx_error;

/**
 * @author ningkang.guo
 * @Date 2019/5/14
 */
public class UnLegalInputException extends Exception {

    public UnLegalInputException(String strMessage) {
        super(strMessage);
    }

    public UnLegalInputException(Throwable throwable) {
        super(throwable);
    }
}
