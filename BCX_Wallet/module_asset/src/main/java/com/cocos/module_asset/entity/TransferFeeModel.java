package com.cocos.module_asset.entity;

import com.cocos.library_base.entity.BaseResult;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ningkang.guo
 * @Date 2019/4/2
 */
public class TransferFeeModel extends BaseResult {

    public FeeBean data;

    public class FeeBean implements Serializable {
        public BigDecimal amount;
        public String asset_id;
    }
}
