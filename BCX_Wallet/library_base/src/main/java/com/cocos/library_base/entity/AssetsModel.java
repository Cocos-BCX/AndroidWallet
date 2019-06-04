package com.cocos.library_base.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ningkang.guo
 * @Date 2019/2/12
 */
@Getter
@Setter
public class AssetsModel extends BaseResult {


    @SerializedName("data")
    public AssetModel data;

    public AssetModel getData() {
        return data;
    }

    public void setData(AssetModel data) {
        this.data = data;
    }


    public static class AssetModel implements Serializable {
        public String dynamic_asset_data_id;
        public String id;
        public String issuer;
        public int precision;
        public String symbol;
        public BigDecimal amount;
        public int operateType;

        @Override
        public boolean equals(Object o) {
            AssetModel that = (AssetModel) o;
            return precision == that.precision &&
                    Objects.equals(dynamic_asset_data_id, that.dynamic_asset_data_id) &&
                    Objects.equals(id, that.id) &&
                    Objects.equals(issuer, that.issuer) &&
                    Objects.equals(symbol, that.symbol) &&
                    Objects.equals(amount, that.amount);
        }
    }
}
