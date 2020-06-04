package com.cocos.bcx_sdk.bcx_wallet.chain;

import java.math.BigDecimal;

/**
 * @author ningkang.guo
 * @Date 2019/8/27
 */
public class limit_orders_object {

    public object_id<limit_orders_object> id;
    public String expiration;
    public object_id<account_object> seller;
    public BigDecimal for_sale;
    public SellPriceBean sell_price;
    public BigDecimal deferred_fee;

    public static class SellPriceBean {
        public asset base;
        public asset quote;
    }
}
