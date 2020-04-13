package com.cocos.library_base.invokedpages.model;

public enum ActionEnum {

    /**
     * 转账
     */
    Transfer("transfer"),

    /**
     * 合约调用
     */
    CallContract("callContract"),

    /**
     * 授权账户
     */
    Authorize("login"),

    /**
     * 消息签名
     */
    SignMessage("signmessage");


    private String value;

    ActionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
