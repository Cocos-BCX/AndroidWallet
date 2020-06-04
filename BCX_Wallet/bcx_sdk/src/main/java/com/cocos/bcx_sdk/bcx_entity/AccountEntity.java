package com.cocos.bcx_sdk.bcx_entity;

import java.io.Serializable;

/**
 * @author ningkang.guo
 * @Date 2019/3/15
 */
public class AccountEntity implements Serializable {

    private AccountBean account;

    public String message;

    public int code;

    public AccountBean getAccount() {
        return account;
    }

    public void setAccount(AccountBean account) {
        this.account = account;
    }

    public static class AccountBean implements Serializable {

        private String id;
        private String name;

        public String getKeystore() {
            return keystore;
        }

        public void setKeystore(String keystore) {
            this.keystore = keystore;
        }

        public String getAccount_type() {
            return account_type;
        }

        public void setAccount_type(String account_type) {
            this.account_type = account_type;
        }

        private String keystore;
        private String account_type;

        public String getChainId() {
            return chainId;
        }

        public void setChainId(String chainId) {
            this.chainId = chainId;
        }

        private String chainId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
