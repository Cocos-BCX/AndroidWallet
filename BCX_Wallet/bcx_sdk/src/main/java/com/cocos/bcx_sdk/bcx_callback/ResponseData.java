package com.cocos.bcx_sdk.bcx_callback;

import com.cocos.bcx_sdk.bcx_wallet.chain.global_config_object;

/**
 * 上层返回数据包装类
 */
public class ResponseData {
    private int code = 0;
    private String message = "";
    private Object data = null;

    public ResponseData(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResponseData() {

    }

    public String toString() {
        ResponseData rd = new ResponseData();
        rd.setCode(this.code);
        rd.setMessage(this.message);
        rd.setData(this.data);
        return global_config_object.getInstance().getGsonBuilder().create().toJson(rd);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
