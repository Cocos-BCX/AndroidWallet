package com.cocos.library_base.entity;

import java.util.List;

/**
 * @author ningkang.guo
 * @Date 2019/12/12
 */
public class AccountEntity extends BaseResult {

    public String id;
    public String membership_expiration_date;
    public String registrar;
    public String name;
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
