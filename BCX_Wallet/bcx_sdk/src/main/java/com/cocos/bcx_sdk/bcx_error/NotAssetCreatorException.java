package com.cocos.bcx_sdk.bcx_error;

/**
 * @author ningkang.guo
 * @Date 2019/8/28
 */
public class NotAssetCreatorException extends Exception {

    public NotAssetCreatorException(String strMessage) {
        super(strMessage);
    }

    public NotAssetCreatorException(Throwable throwable) {
        super(throwable);
    }

}
