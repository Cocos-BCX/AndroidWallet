package com.cocos.bcx_sdk.bcx_wallet.chain;

import java.util.List;

/**
 * @author ningkang.guo
 * @Date 2019/4/15
 */
public class nhasset_object {

    public object_id<nhasset_object> id;
    public String nh_hash;
    public object_id<account_object> nh_asset_creator;
    public object_id<account_object> nh_asset_owner;
    public object_id<account_object> nh_asset_active;
    public object_id<account_object> authority_account;
    public String asset_qualifier;
    public String world_view;
    public String base_describe;
    public String create_time;
    public String limit_type;
    public List<Object> parent;
    public List<Object> child;
    public List<Object> describe_with_contract;
    public List<Object> limit_list;

}
