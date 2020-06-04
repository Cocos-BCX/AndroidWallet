package com.cocos.bcx_sdk.bcx_wallet.chain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ningkang.guo
 * @Date 2019/6/11
 */
public class operation_results_object {

    public long real_running_time;
    public object_id<contract_object> contract_id;
    public boolean existed_pv;
    public String process_value;
    public asset_fee_object additional_cost;
    public List<Object> contract_affecteds = new ArrayList<>();
}
