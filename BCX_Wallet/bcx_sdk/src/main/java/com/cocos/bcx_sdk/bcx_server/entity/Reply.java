package com.cocos.bcx_sdk.bcx_server.entity;

import com.cocos.bcx_sdk.bcx_server.error.WebSocketError;

/**
 * webSocket reply message data
 */
public class Reply<T> {
    public String id;
    public String jsonrpc;
    public T result;
    public WebSocketError error;
}