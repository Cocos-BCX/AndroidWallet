package com.cocos.module_asset.entity;

import java.io.Serializable;

/**
 * @author ningkang.guo
 * @Date 2019/4/2
 */
public class DealDetailModel implements Serializable {
    public String amount;
    public String deal_type;
    public String from;
    public String to;
    public DealRecordModel.OpBean.MemoBean memo;
    public String fee;
    public String trx_hash;
    public String block_header;
    public String time;
    public String amountAssetSymbol;
    public String feeAssetSymbol;
}
