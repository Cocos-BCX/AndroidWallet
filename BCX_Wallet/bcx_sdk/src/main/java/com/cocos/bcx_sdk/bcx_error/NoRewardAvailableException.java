package com.cocos.bcx_sdk.bcx_error;

/**
 * @author ningkang.guo
 * @Date 2019/9/25
 */
public class NoRewardAvailableException extends Throwable {

    public NoRewardAvailableException(String strMessage) {
        super(strMessage);
    }

    public NoRewardAvailableException(Throwable throwable) {
        super(throwable);
    }
}
