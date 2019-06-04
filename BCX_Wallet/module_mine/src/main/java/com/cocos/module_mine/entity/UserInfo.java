package com.cocos.module_mine.entity;

import com.cocos.library_base.entity.BaseResult;

import java.util.List;

/**
 * @author ningkang.guo
 * @Date 2019/2/28
 */
public class UserInfo extends BaseResult {


    /**
     * data : {"account":{"id":"1.2.769","membership_expiration_date":"1970-01-01T00:00:00","registrar":"1.2.17","referrer":"1.2.17","lifetime_referrer":"1.2.17","network_fee_percentage":2000,"lifetime_referrer_fee_percentage":3000,"referrer_rewards_percentage":0,"name":"gnkhandsome1","owner":{"weight_threshold":1,"account_auths":[],"key_auths":[["COCOS8Dw7QjWVFggYCvp9c8XbsXssqizN1MqkwPfSAVTQppQLhUcTC2",1]],"address_auths":[]},"active":{"weight_threshold":1,"account_auths":[],"key_auths":[["COCOS6G55VgR94GZmELS4UHEf2eVggmhPRnWLTWgGiEmzuBKdvEwoAB",1]],"address_auths":[]},"options":{"memo_key":"COCOS6G55VgR94GZmELS4UHEf2eVggmhPRnWLTWgGiEmzuBKdvEwoAB","voting_account":"1.2.5","num_witness":0,"num_committee":0,"votes":[],"extensions":[]},"statistics":"2.6.769","contract_asset_locked":{"locked_total":[],"lock_details":[]},"whitelisting_accounts":[],"blacklisting_accounts":[],"whitelisted_accounts":[],"blacklisted_accounts":[],"owner_special_authority":[0,{}],"active_special_authority":[0,{}],"top_n_control_flags":0},"statistics":{"id":"2.6.769","owner":"1.2.769","most_recent_op":"2.9.10701638","total_ops":4,"removed_ops":0,"total_core_in_orders":0,"lifetime_fees_paid":408984,"pending_fees":0,"pending_vested_fees":0},"registrar_name":"official-account","referrer_name":"official-account","lifetime_referrer_name":"official-account","votes":[],"balances":[{"id":"2.5.748","owner":"1.2.769","asset_type":"1.3.0","balance":979591016}],"vesting_balances":[],"limit_orders":[],"call_orders":[],"settle_orders":[],"proposals":[],"assets":[],"withdraws":[]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * account : {"id":"1.2.769","membership_expiration_date":"1970-01-01T00:00:00","registrar":"1.2.17","referrer":"1.2.17","lifetime_referrer":"1.2.17","network_fee_percentage":2000,"lifetime_referrer_fee_percentage":3000,"referrer_rewards_percentage":0,"name":"gnkhandsome1","owner":{"weight_threshold":1,"account_auths":[],"key_auths":[["COCOS8Dw7QjWVFggYCvp9c8XbsXssqizN1MqkwPfSAVTQppQLhUcTC2",1]],"address_auths":[]},"active":{"weight_threshold":1,"account_auths":[],"key_auths":[["COCOS6G55VgR94GZmELS4UHEf2eVggmhPRnWLTWgGiEmzuBKdvEwoAB",1]],"address_auths":[]},"options":{"memo_key":"COCOS6G55VgR94GZmELS4UHEf2eVggmhPRnWLTWgGiEmzuBKdvEwoAB","voting_account":"1.2.5","num_witness":0,"num_committee":0,"votes":[],"extensions":[]},"statistics":"2.6.769","contract_asset_locked":{"locked_total":[],"lock_details":[]},"whitelisting_accounts":[],"blacklisting_accounts":[],"whitelisted_accounts":[],"blacklisted_accounts":[],"owner_special_authority":[0,{}],"active_special_authority":[0,{}],"top_n_control_flags":0}
         * statistics : {"id":"2.6.769","owner":"1.2.769","most_recent_op":"2.9.10701638","total_ops":4,"removed_ops":0,"total_core_in_orders":0,"lifetime_fees_paid":408984,"pending_fees":0,"pending_vested_fees":0}
         * registrar_name : official-account
         * referrer_name : official-account
         * lifetime_referrer_name : official-account
         * votes : []
         * balances : [{"id":"2.5.748","owner":"1.2.769","asset_type":"1.3.0","balance":979591016}]
         * vesting_balances : []
         * limit_orders : []
         * call_orders : []
         * settle_orders : []
         * proposals : []
         * assets : []
         * withdraws : []
         */

        private AccountBean account;
        private StatisticsBean statistics;
        private String registrar_name;
        private String referrer_name;
        private String lifetime_referrer_name;
        private List<BalancesBean> balances;

        public AccountBean getAccount() {
            return account;
        }

        public void setAccount(AccountBean account) {
            this.account = account;
        }

        public StatisticsBean getStatistics() {
            return statistics;
        }

        public void setStatistics(StatisticsBean statistics) {
            this.statistics = statistics;
        }

        public String getRegistrar_name() {
            return registrar_name;
        }

        public void setRegistrar_name(String registrar_name) {
            this.registrar_name = registrar_name;
        }

        public String getReferrer_name() {
            return referrer_name;
        }

        public void setReferrer_name(String referrer_name) {
            this.referrer_name = referrer_name;
        }

        public String getLifetime_referrer_name() {
            return lifetime_referrer_name;
        }

        public void setLifetime_referrer_name(String lifetime_referrer_name) {
            this.lifetime_referrer_name = lifetime_referrer_name;
        }

        public List<BalancesBean> getBalances() {
            return balances;
        }

        public void setBalances(List<BalancesBean> balances) {
            this.balances = balances;
        }

        public static class AccountBean {
            /**
             * id : 1.2.769
             * membership_expiration_date : 1970-01-01T00:00:00
             * registrar : 1.2.17
             * referrer : 1.2.17
             * lifetime_referrer : 1.2.17
             * network_fee_percentage : 2000
             * lifetime_referrer_fee_percentage : 3000
             * referrer_rewards_percentage : 0
             * name : gnkhandsome1
             * owner : {"weight_threshold":1,"account_auths":[],"key_auths":[["COCOS8Dw7QjWVFggYCvp9c8XbsXssqizN1MqkwPfSAVTQppQLhUcTC2",1]],"address_auths":[]}
             * active : {"weight_threshold":1,"account_auths":[],"key_auths":[["COCOS6G55VgR94GZmELS4UHEf2eVggmhPRnWLTWgGiEmzuBKdvEwoAB",1]],"address_auths":[]}
             * options : {"memo_key":"COCOS6G55VgR94GZmELS4UHEf2eVggmhPRnWLTWgGiEmzuBKdvEwoAB","voting_account":"1.2.5","num_witness":0,"num_committee":0,"votes":[],"extensions":[]}
             * statistics : 2.6.769
             * contract_asset_locked : {"locked_total":[],"lock_details":[]}
             * whitelisting_accounts : []
             * blacklisting_accounts : []
             * whitelisted_accounts : []
             * blacklisted_accounts : []
             * owner_special_authority : [0,{}]
             * active_special_authority : [0,{}]
             * top_n_control_flags : 0
             */

            private String id;
            private String membership_expiration_date;
            private String registrar;
            private String referrer;
            private String lifetime_referrer;
            private int network_fee_percentage;
            private int lifetime_referrer_fee_percentage;
            private int referrer_rewards_percentage;
            private String name;
            private OwnerBean owner;
            private ActiveBean active;
            private OptionsBean options;
            private String statistics;
            private int top_n_control_flags;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getMembership_expiration_date() {
                return membership_expiration_date;
            }

            public void setMembership_expiration_date(String membership_expiration_date) {
                this.membership_expiration_date = membership_expiration_date;
            }

            public String getRegistrar() {
                return registrar;
            }

            public void setRegistrar(String registrar) {
                this.registrar = registrar;
            }

            public String getReferrer() {
                return referrer;
            }

            public void setReferrer(String referrer) {
                this.referrer = referrer;
            }

            public String getLifetime_referrer() {
                return lifetime_referrer;
            }

            public void setLifetime_referrer(String lifetime_referrer) {
                this.lifetime_referrer = lifetime_referrer;
            }

            public int getNetwork_fee_percentage() {
                return network_fee_percentage;
            }

            public void setNetwork_fee_percentage(int network_fee_percentage) {
                this.network_fee_percentage = network_fee_percentage;
            }

            public int getLifetime_referrer_fee_percentage() {
                return lifetime_referrer_fee_percentage;
            }

            public void setLifetime_referrer_fee_percentage(int lifetime_referrer_fee_percentage) {
                this.lifetime_referrer_fee_percentage = lifetime_referrer_fee_percentage;
            }

            public int getReferrer_rewards_percentage() {
                return referrer_rewards_percentage;
            }

            public void setReferrer_rewards_percentage(int referrer_rewards_percentage) {
                this.referrer_rewards_percentage = referrer_rewards_percentage;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public OwnerBean getOwner() {
                return owner;
            }

            public void setOwner(OwnerBean owner) {
                this.owner = owner;
            }

            public ActiveBean getActive() {
                return active;
            }

            public void setActive(ActiveBean active) {
                this.active = active;
            }

            public OptionsBean getOptions() {
                return options;
            }

            public void setOptions(OptionsBean options) {
                this.options = options;
            }

            public String getStatistics() {
                return statistics;
            }

            public void setStatistics(String statistics) {
                this.statistics = statistics;
            }

            public int getTop_n_control_flags() {
                return top_n_control_flags;
            }

            public void setTop_n_control_flags(int top_n_control_flags) {
                this.top_n_control_flags = top_n_control_flags;
            }

            public static class OwnerBean {
                /**
                 * weight_threshold : 1
                 * account_auths : []
                 * key_auths : [["COCOS8Dw7QjWVFggYCvp9c8XbsXssqizN1MqkwPfSAVTQppQLhUcTC2",1]]
                 * address_auths : []
                 */

                private int weight_threshold;
                private List<List<String>> key_auths;

                public int getWeight_threshold() {
                    return weight_threshold;
                }

                public void setWeight_threshold(int weight_threshold) {
                    this.weight_threshold = weight_threshold;
                }

                public List<List<String>> getKey_auths() {
                    return key_auths;
                }

                public void setKey_auths(List<List<String>> key_auths) {
                    this.key_auths = key_auths;
                }
            }

            public static class ActiveBean {
                /**
                 * weight_threshold : 1
                 * account_auths : []
                 * key_auths : [["COCOS6G55VgR94GZmELS4UHEf2eVggmhPRnWLTWgGiEmzuBKdvEwoAB",1]]
                 * address_auths : []
                 */

                private int weight_threshold;
                private List<List<String>> key_auths;

                public int getWeight_threshold() {
                    return weight_threshold;
                }

                public void setWeight_threshold(int weight_threshold) {
                    this.weight_threshold = weight_threshold;
                }


                public List<List<String>> getKey_auths() {
                    return key_auths;
                }

                public void setKey_auths(List<List<String>> key_auths) {
                    this.key_auths = key_auths;
                }

            }

            public static class OptionsBean {
                /**
                 * memo_key : COCOS6G55VgR94GZmELS4UHEf2eVggmhPRnWLTWgGiEmzuBKdvEwoAB
                 * voting_account : 1.2.5
                 * num_witness : 0
                 * num_committee : 0
                 * votes : []
                 * extensions : []
                 */

                private String memo_key;
                private String voting_account;
                private int num_witness;
                private int num_committee;

                public String getMemo_key() {
                    return memo_key;
                }

                public void setMemo_key(String memo_key) {
                    this.memo_key = memo_key;
                }

                public String getVoting_account() {
                    return voting_account;
                }

                public void setVoting_account(String voting_account) {
                    this.voting_account = voting_account;
                }

                public int getNum_witness() {
                    return num_witness;
                }

                public void setNum_witness(int num_witness) {
                    this.num_witness = num_witness;
                }

                public int getNum_committee() {
                    return num_committee;
                }

                public void setNum_committee(int num_committee) {
                    this.num_committee = num_committee;
                }
            }
        }

        public static class StatisticsBean {
            /**
             * id : 2.6.769
             * owner : 1.2.769
             * most_recent_op : 2.9.10701638
             * total_ops : 4
             * removed_ops : 0
             * total_core_in_orders : 0
             * lifetime_fees_paid : 408984
             * pending_fees : 0
             * pending_vested_fees : 0
             */

            private String id;
            private String owner;
            private String most_recent_op;
            private int total_ops;
            private int removed_ops;
            private int total_core_in_orders;
            private int lifetime_fees_paid;
            private int pending_fees;
            private int pending_vested_fees;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getOwner() {
                return owner;
            }

            public void setOwner(String owner) {
                this.owner = owner;
            }

            public String getMost_recent_op() {
                return most_recent_op;
            }

            public void setMost_recent_op(String most_recent_op) {
                this.most_recent_op = most_recent_op;
            }

            public int getTotal_ops() {
                return total_ops;
            }

            public void setTotal_ops(int total_ops) {
                this.total_ops = total_ops;
            }

            public int getRemoved_ops() {
                return removed_ops;
            }

            public void setRemoved_ops(int removed_ops) {
                this.removed_ops = removed_ops;
            }

            public int getTotal_core_in_orders() {
                return total_core_in_orders;
            }

            public void setTotal_core_in_orders(int total_core_in_orders) {
                this.total_core_in_orders = total_core_in_orders;
            }

            public int getLifetime_fees_paid() {
                return lifetime_fees_paid;
            }

            public void setLifetime_fees_paid(int lifetime_fees_paid) {
                this.lifetime_fees_paid = lifetime_fees_paid;
            }

            public int getPending_fees() {
                return pending_fees;
            }

            public void setPending_fees(int pending_fees) {
                this.pending_fees = pending_fees;
            }

            public int getPending_vested_fees() {
                return pending_vested_fees;
            }

            public void setPending_vested_fees(int pending_vested_fees) {
                this.pending_vested_fees = pending_vested_fees;
            }
        }

        public static class BalancesBean {
            /**
             * id : 2.5.748
             * owner : 1.2.769
             * asset_type : 1.3.0
             * balance : 979591016
             */

            private String id;
            private String owner;
            private String asset_type;
            private int balance;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getOwner() {
                return owner;
            }

            public void setOwner(String owner) {
                this.owner = owner;
            }

            public String getAsset_type() {
                return asset_type;
            }

            public void setAsset_type(String asset_type) {
                this.asset_type = asset_type;
            }

            public int getBalance() {
                return balance;
            }

            public void setBalance(int balance) {
                this.balance = balance;
            }
        }
    }
}
