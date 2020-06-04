package com.cocos.bcx_sdk.bcx_server.entity;

import java.util.List;

/**
 * websocket send message data
 */
public class Call {

    /**
     * 自定义ID又来分辨返回数据
     */
   public int id;

    /**
     * RPC
     */
    public String method;

    public List<Object> params;

}