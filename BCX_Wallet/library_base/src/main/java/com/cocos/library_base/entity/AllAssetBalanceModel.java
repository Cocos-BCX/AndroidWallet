package com.cocos.library_base.entity;

import com.cocos.library_base.entity.BaseResult;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ningkang.guo
 * @Date 2019/4/2
 */
public class AllAssetBalanceModel extends BaseResult {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        private BigDecimal amount;
        private String asset_id;

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getAsset_id() {
            return asset_id;
        }

        public void setAsset_id(String asset_id) {
            this.asset_id = asset_id;
        }
    }
}
