package com.cocos.bcx_sdk.bcx_server.callback;


/**
 * socket 返回数据处理
 *
 * @param <T>
 */
public interface IReplyObjectProcess<T> {

    public void processTextToObject(String strText);

    public  T getReplyObject();

    public  String getError();

    public  void notifyFailure(Throwable t);

    public  Throwable getException();

    public String getResponse();
}