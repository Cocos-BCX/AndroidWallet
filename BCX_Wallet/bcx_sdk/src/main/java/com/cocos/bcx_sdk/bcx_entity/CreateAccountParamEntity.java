package com.cocos.bcx_sdk.bcx_entity;

import com.cocos.bcx_sdk.bcx_utils.bitlib.util.StringUtils;

/**
 * @author ningkang.guo
 * @Date 2019/3/14
 */
public class CreateAccountParamEntity {

    private String accountName;

    private String password;

    public AccountType getAccountType() {
        return accountType;
    }

    private AccountType accountType;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getActiveSeed() {
        if (this.accountType == AccountType.ACCOUNT) {
            return this.accountName + "active" + getPassword();
        } else if (this.accountType == AccountType.WALLET) {
            return StringUtils.getRandomString(60);
        }
        return null;
    }

    public String getOwnerSeed() {
        if (this.accountType == AccountType.ACCOUNT) {
            return this.accountName + "owner" + getPassword();
        } else if (this.accountType == AccountType.WALLET) {
            return StringUtils.getRandomString(60);
        }
        return null;
    }
}
