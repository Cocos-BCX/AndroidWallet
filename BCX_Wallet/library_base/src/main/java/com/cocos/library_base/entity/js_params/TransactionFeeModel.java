package com.cocos.library_base.entity.js_params;

/**
 * @author ningkang.guo
 * @Date 2019/5/15
 */
public class TransactionFeeModel {

    public int code;
    public DataBean data;
    public String message;

    public static class DataBean {
        public double fee_amount;
        public String fee_symbol;
    }
}
