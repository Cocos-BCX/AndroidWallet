package com.cocos.bcx_sdk.bcx_wallet.chain;


import java.util.List;


/**
 * 用户操作记录返回数据体
 */
public class operation_history_object {

    public String id;
    public int block_num;
    public int trx_in_block;
    public int op_in_trx;
    public int virtual_op;
    public List<Object> op;
    public List<Object> result;

}
