package com.cocos.module_login.entity;

import com.cocos.library_base.entity.BaseResult;

/**
 * @author ningkang.guo
 * @Date 2019/2/28
 */
public class LoginModel extends BaseResult {


    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private String lifetime_referrer;
        private int lifetime_referrer_fee_percentage;
        private String membership_expiration_date;
        private String name;
        private int network_fee_percentage;
        private String referrer;
        private int referrer_rewards_percentage;
        private String registrar;
        private String statistics;


        public String getLifetime_referrer() {
            return lifetime_referrer;
        }

        public void setLifetime_referrer(String lifetime_referrer) {
            this.lifetime_referrer = lifetime_referrer;
        }

        public int getLifetime_referrer_fee_percentage() {
            return lifetime_referrer_fee_percentage;
        }

        public void setLifetime_referrer_fee_percentage(int lifetime_referrer_fee_percentage) {
            this.lifetime_referrer_fee_percentage = lifetime_referrer_fee_percentage;
        }

        public String getMembership_expiration_date() {
            return membership_expiration_date;
        }

        public void setMembership_expiration_date(String membership_expiration_date) {
            this.membership_expiration_date = membership_expiration_date;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNetwork_fee_percentage() {
            return network_fee_percentage;
        }

        public void setNetwork_fee_percentage(int network_fee_percentage) {
            this.network_fee_percentage = network_fee_percentage;
        }

        public String getReferrer() {
            return referrer;
        }

        public void setReferrer(String referrer) {
            this.referrer = referrer;
        }

        public int getReferrer_rewards_percentage() {
            return referrer_rewards_percentage;
        }

        public void setReferrer_rewards_percentage(int referrer_rewards_percentage) {
            this.referrer_rewards_percentage = referrer_rewards_percentage;
        }

        public String getRegistrar() {
            return registrar;
        }

        public void setRegistrar(String registrar) {
            this.registrar = registrar;
        }

        public String getStatistics() {
            return statistics;
        }

        public void setStatistics(String statistics) {
            this.statistics = statistics;
        }

    }
}
