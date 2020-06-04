package com.cocos.bcx_sdk.bcx_wallet.chain;

import java.util.List;

/**
 * @author ningkang.guo
 * @Date 2019/10/16
 */
public class witnesses_object_result {

    public String account_name;
    public String url;
    public String votes;
    public boolean active;
    public boolean supported;
    public String account_id;
    public String type;
    public String vote_id;
    public String witness_id;
    public long last_aslot;
    public long last_confirmed_block_num;
    public long total_missed;
    public List<witnesses_object_result.SupportersBean> supporters;

    public static class SupportersBean {
        public object_id<account_object> account_id;
        public asset_fee_object amount_raw;
        public String amount_text;
    }

}
