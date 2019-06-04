package com.cocos.library_base.entity;

import java.math.BigDecimal;

public class AssetBalanceModel extends BaseResult {

    public DataBean data;

    public static class DataBean {
        public BigDecimal amount;
        public String asset_id;
    }
}