package com.cocos.library_base.entity.js_params;

import java.util.List;

/**
 * @author ningkang.guo
 * @Date 2019/5/15
 */
public class TransactionModel {

    public int code;
    public String message;
    public TrxDataBean trx_data;
    public List<DataBean> data;

    public static class TrxDataBean {
        public String trx_id;
        public long block_num;
    }

    public static class DataBean {
        public long real_running_time;
    }
}
