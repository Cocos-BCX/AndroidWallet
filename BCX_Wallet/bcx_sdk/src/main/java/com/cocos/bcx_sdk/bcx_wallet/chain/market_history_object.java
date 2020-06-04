package com.cocos.bcx_sdk.bcx_wallet.chain;

/**
 * @author ningkang.guo
 * @Date 2019/8/28
 */
public class market_history_object {

    public String id;
    public KeyBean key;
    public int high_base;
    public int high_quote;
    public int low_base;
    public int low_quote;
    public int open_base;
    public int open_quote;
    public int close_base;
    public int close_quote;
    public int base_volume;
    public int quote_volume;

    public static class KeyBean {

        public String base;
        public String quote;
        public int seconds;
        public String open;

    }
}
