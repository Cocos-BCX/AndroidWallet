package com.cocos.bcx_sdk.bcx_wallet.chain;

/**
 * @author ningkang.guo
 * @Date 2019/6/11
 */
enum ColorEnum {

    资产(0, "资产"), nh资产(1, "nh资产"), 备注(2, "备注"), 日志(3, "日志");
    //防止字段值被修改，增加的字段也统一final表示常量
    private final int key;
    private final String value;

    private ColorEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

}
