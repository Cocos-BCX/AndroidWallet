package com.cocos.bcx_sdk.bcx_wallet.chain;

import java.util.List;

/**
 * @author ningkang.guo
 * @Date 2019/4/15
 */
public class contract_object {
    public object_id<contract_object> id;
    public String name;
    public String creation_date;
    public String owner;
    public String current_version;
    public String contract_authority;
    public boolean check_contract_authority;
    public List<List<Object>> contract_data;
    public List<List<Object>> contract_ABI;
    public String lua_code_b_id;
}
