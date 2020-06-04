package com.cocos.bcx_sdk.bcx_wallet.chain;

import java.util.Date;
import java.util.HashMap;

/**
 * @author ningkang.guo
 * @Date 2019/10/16
 */
public class witnesses_object {

    public object_id<witnesses_object> id;
    public object_id<account_object> witness_account;
    public int last_aslot;
    public String signing_key;
    public String pay_vb;
    public String vote_id;
    public long total_votes;
    public String url;
    public long total_missed;
    public long last_confirmed_block_num;
    public boolean work_status;
    public Date next_maintenance_time;
    public HashMap<object_id<account_object>, asset_fee_object> supporters = new HashMap<>();

}
