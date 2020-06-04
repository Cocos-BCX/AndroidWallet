package com.cocos.bcx_sdk.bcx_wallet.chain;

import java.util.List;
import java.util.Set;

/**
 * @author ningkang.guo
 * @Date 2019/6/11
 */
public class contract_operations {

    public asset fee;
    public object_id<account_object> caller;
    public object_id<contract_object> contract_id;
    public String function_name;
    public List<List<Object>> value_list;
    public Set<types.void_t> extensions;

}
