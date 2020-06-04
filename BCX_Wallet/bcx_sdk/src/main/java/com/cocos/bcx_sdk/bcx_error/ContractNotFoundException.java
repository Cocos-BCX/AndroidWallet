package com.cocos.bcx_sdk.bcx_error;

/**
 * @author ningkang.guo
 * @Date 2019/4/15
 */
public class ContractNotFoundException extends Exception {

    public ContractNotFoundException(String strMessage) {
        super(strMessage);
    }

    public ContractNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
