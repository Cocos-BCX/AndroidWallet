package com.cocos.bcx_sdk.bcx_wallet.chain;

import com.google.gson.JsonArray;

import java.util.List;

/**
 * @author ningkang.guo
 * @Date 2019/9/26
 */
public class vesting_balances_object {

    public object_id<vesting_balances_object> id;
    public String owner;
    public asset balance;
    public String describe;
    public JsonArray policy;
}
