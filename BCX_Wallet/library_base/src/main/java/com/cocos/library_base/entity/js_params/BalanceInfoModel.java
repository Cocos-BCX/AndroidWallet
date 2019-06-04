package com.cocos.library_base.entity.js_params;

/**
 * js 获取当前账户信息
 *
 * @author ningkang.guo
 * @Date 2019/5/5
 */
public class BalanceInfoModel {


    /**
     * account_id : COCOS
     */
    public DataInfo data;

    public int code;

    public String message;

    public static class DataInfo {

        /**
         * amount : COCOS
         */
        public String COCOS = "0";

    }


}
