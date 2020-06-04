package com.cocos.bcx_sdk.bcx_wallet.chain;

/**
 * @author ningkang.guo
 * @Date 2019/4/15
 */
public class nh_asset_order_object {

    public object_id<nh_asset_order_object> id;
    public object_id<account_object> seller;
    public object_id<account_object> otcaccount;
    public object_id<nhasset_object> nh_asset_id;
    public String asset_qualifier;
    public String world_view;
    public String base_describe;
    public String nh_hash;
    public asset price;
    public String memo;
    public String expiration;
}
