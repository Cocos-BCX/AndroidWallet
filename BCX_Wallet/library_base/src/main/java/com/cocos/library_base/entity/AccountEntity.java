package com.cocos.library_base.entity;

import com.cocos.bcx_sdk.bcx_wallet.chain.account_object;
import com.cocos.bcx_sdk.bcx_wallet.chain.authority;
import com.cocos.bcx_sdk.bcx_wallet.chain.object_id;
import com.cocos.bcx_sdk.bcx_wallet.chain.types;

import java.util.List;

/**
 * @author ningkang.guo
 * @Date 2019/12/12
 */
public class AccountEntity extends BaseResult {

    public object_id<account_object> id;
    public String membership_expiration_date;
    public String registrar;
    public String name;
    public authority owner;
    public authority active;
    public types.account_options options;
    public String statistics;
    public AssetLockedBean asset_locked;
    public String cashback_gas;
    public String cashback_vote;
    public int top_n_control_flags;

    public static class AssetLockedBean {
        public VoteForWitnessBean vote_for_witness;
        public List<List<String>> locked_total;
        public List<Object> contract_lock_details;

        public static class VoteForWitnessBean {
            public int amount;
            public String asset_id;
        }
    }
}
