package com.cocos.library_base.entity.js_response;

/**
 * js返回的转账参数实体
 *
 * @author ningkang.guo
 * @Date 2019/5/5
 */
public class TransferParamModel {

    public String fromAccount;
    public String toAccount;
    public String amount;
    public String assetId;
    public String memo;
    public String feeAssetId;
    public boolean isPropose;
    public boolean onlyGetFee;
}
