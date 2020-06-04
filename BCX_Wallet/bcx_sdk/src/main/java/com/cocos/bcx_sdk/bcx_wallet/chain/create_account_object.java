package com.cocos.bcx_sdk.bcx_wallet.chain;

/**
 * create account response data entity
 */
public class create_account_object {

    public int code;
    public DataBean data;
    public String msg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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
            private String id;
            private String name;
            private String owner_key;

            public String getActive_key() {
                return active_key;
            }

            public void setActive_key(String active_key) {
                this.active_key = active_key;
            }

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

            public String getOwner_key() {
                return owner_key;
            }

            public void setOwner_key(String owner_key) {
                this.owner_key = owner_key;
            }
        }
    }
}
