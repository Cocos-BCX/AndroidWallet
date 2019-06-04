package com.cocos.module_asset.entity;

import com.cocos.library_base.entity.BaseResult;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ningkang.guo
 * @Date 2019/2/25
 */

@Getter
@Setter
public class TransferParamsModel extends BaseResult {


    /**
     * 转出账号
     */
    private String accountName;

    /**
     * 转入账号
     */
    private String receivablesAccountName;

    /**
     * 账户余额
     */
    private String accountBalance;

    /**
     * 转账金额
     */
    private String transferAmount;

    /**
     * 转账备注
     */
    private String transferMemo;

    /**
     * 转账费用
     */
    private String fee;


    private String password;

    private String feeSymbol;

    private String transferSymbol;

}
