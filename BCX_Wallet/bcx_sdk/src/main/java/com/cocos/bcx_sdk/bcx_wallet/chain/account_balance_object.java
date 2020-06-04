package com.cocos.bcx_sdk.bcx_wallet.chain;

public class account_balance_object {
    public object_id id;
    public object_id<account_object> owner;
    public object_id<asset_object> asset_type;
    public long balance;
}