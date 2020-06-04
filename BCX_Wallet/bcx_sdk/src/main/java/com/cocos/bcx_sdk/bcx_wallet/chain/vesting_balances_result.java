package com.cocos.bcx_sdk.bcx_wallet.chain;

/**
 * @author ningkang.guo
 * @Date 2019/9/29
 */
public class vesting_balances_result {

    public object_id<vesting_balances_object> id;
    public double return_cash;
    public String earned_coindays;
    public String require_coindays;
    public String remaining_days;
    public String available_percent;
    public AvailableBalanceBean available_balance;

    public static class AvailableBalanceBean {

        public String amount;
        public String asset_id;
        public String symbol;
        public int precision;

    }
}
