package com.cocos.bcx_sdk.bcx_wallet.chain;

import java.util.List;

/**
 * created by Jiang on 2020/7/16 10:08
 */
public class RecallbackJsModel {
    public String contract_id;
    public List<contract_callback.DataBean.ContractAffectedsBean> contract_affecteds;
    public Long real_running_time;
    public boolean existed_pv;
    public String process_value;
    public Object additional_cost;
}
