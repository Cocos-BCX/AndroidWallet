package com.cocos.bcx_sdk.bcx_wallet.chain;

import java.util.List;


public class global_property_object {

    public object_id id;
    public int next_available_vote_id = 0;
    public ParametersBean parameters;
    public List<object_id> active_committee_members;
    public List<object_id> active_witnesses;

    public static class ParametersBean {

        public long block_interval;
        public long maintenance_interval;
        public long maintenance_skip_slots;
        public long committee_proposal_review_period;
        public long maximum_transaction_size;
        public long maximum_block_size;
        public long maximum_time_until_expiration;
        public long maximum_proposal_lifetime;
        public long maximum_asset_feed_publishers;
        public long maximum_witness_count;
        public long maximum_committee_count;
        public long maximum_authority_membership;
        public long committee_percent_of_network;
        public long network_percent_of_fee;
        public long lifetime_referrer_percent_of_fee;
        public long cashback_vesting_period_seconds;
        public long cashback_vesting_threshold;
        public boolean count_non_member_votes;
        public boolean allow_non_member_whitelists;
        public long witness_pay_per_block;
        public String worker_budget_per_day;
        public long max_predicate_opcode;
        public long fee_liquidation_threshold;
        public long accounts_per_fee_scale;
        public long account_fee_scale_bitshifts;
        public long max_authority_depth;
        public long maximum_run_time_ratio;
        public long voting_adoption_ratio;
        public long maximum_nh_asset_order_expiration;
        public long assigned_task_life_cycle;
        public long crontab_suspend_threshold;
        public long crontab_suspend_expiration;
    }

}
