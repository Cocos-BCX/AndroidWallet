package com.cocos.bcx_sdk.bcx_wallet.chain;

import java.util.Date;
import java.util.HashMap;

/**
 * @author ningkang.guo
 * @Date 2019/10/16
 */
public class committee_object {

    public object_id<committee_object> id;
    public object_id<account_object> committee_member_account;
    public String vote_id;
    public long total_votes;
    public String url;
    public boolean work_status;
    public Date next_maintenance_time;
    public HashMap<object_id<account_object>, asset_fee_object> supporters = new HashMap<>();

}
