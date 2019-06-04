package com.cocos.module_login.entity;

import com.cocos.library_base.entity.BaseResult;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ningkang.guo
 * @Date 2019/1/31
 */

@Getter
@Setter
public class RegisterModel extends BaseResult {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private AccountBean account;

        public AccountBean getAccount() {
            return account;
        }

        public void setAccount(AccountBean account) {
            this.account = account;
        }

        public static class AccountBean {

            private String active_key;
            private String name;
            private String owner_key;

            public String getActive_key() {
                return active_key;
            }

            public void setActive_key(String active_key) {
                this.active_key = active_key;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getOwner_key() {
                return owner_key;
            }

            public void setOwner_key(String owner_key) {
                this.owner_key = owner_key;
            }
        }
    }
}
