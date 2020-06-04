package com.cocos.bcx_sdk.bcx_wallet.chain;

/**
 * @author ningkang.guo
 * @Date 2019/8/27
 */
public class fill_order_history_object {

    public String id;
    public KeyBean key;
    public String time;
    public OpBean op;

    public static class KeyBean {
        public String base;
        public String quote;
        public int sequence;
    }

    public static class OpBean {

        public asset fee;
        public String order_id;
        public object_id<account_object> account_id;
        public asset pays;
        public asset receives;
        public FillPriceBean fill_price;
        public boolean is_maker;


        public static class FillPriceBean {
            public asset base;
            public asset quote;
        }
    }
}
