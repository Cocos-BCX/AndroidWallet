package com.cocos.library_base.entity;

import android.text.TextUtils;

import com.cocos.library_base.global.SPKeyGlobal;
import com.cocos.library_base.utils.SPUtils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
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
        public String frozen_asset;
        public int precision;
        public String symbol;
        public BigDecimal amount;
        public BigDecimal useable_amount;
        public BigDecimal price;
        public int operateType;


        public String getFrozen_asset(String asset_id) {
            try {
                NumberFormat nf = NumberFormat.getInstance();
                List<FullAccountsDataModel.AssetModel> locked_asset = SPUtils.getLockedAssetInfo(SPKeyGlobal.TOTAL_LOCK_ASSET);
                if (null == locked_asset || locked_asset.size() <= 0) {
                    return "0.00";
                }
                for (FullAccountsDataModel.AssetModel assetModel : locked_asset) {
                    if (TextUtils.equals(assetModel.asset_id, asset_id)) {
                        if (assetModel.precision > 5) {
                            return nf.format(Double.valueOf(new BigDecimal(assetModel.amount).setScale(5, BigDecimal.ROUND_HALF_UP).toString()));
                        }
                        return nf.format(Double.valueOf(assetModel.amount));
                    }
                }
            } catch (Exception e) {

            }
            return "0.00";
        }

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
