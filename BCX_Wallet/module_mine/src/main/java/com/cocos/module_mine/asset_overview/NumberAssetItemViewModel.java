package com.cocos.module_mine.asset_overview;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.cocos.library_base.base.ItemViewModel;
import com.cocos.library_base.entity.AssetsModel;
import com.cocos.library_base.global.SPKeyGlobal;
import com.cocos.library_base.utils.CurrencyUtils;
import com.cocos.library_base.utils.SPUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.module_mine.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * @author ningkang.guo
 * @Date 2019/2/25
 */
public class NumberAssetItemViewModel extends ItemViewModel {

    public ObservableField<AssetsModel.AssetModel> entity = new ObservableField<>();
    public Drawable drawableImg;
    public ObservableField<String> totalValue = new ObservableField<>("≈ ￥0.00");
    public ObservableField<String> symbolType = new ObservableField<>("");
    public ObservableField<String> amount = new ObservableField<>("0.00");
    public ObservableField<String> frozenAmount = new ObservableField<>("冻结 0.00");
    public ObservableInt frozenAmountViewVisible = new ObservableInt(View.GONE);

    public NumberAssetItemViewModel(@NonNull NumberAssetViewModel viewModel, AssetsModel.AssetModel entity) {
        super(viewModel);
        String netType = SPUtils.getString(Utils.getContext(), SPKeyGlobal.NET_TYPE, "");
        symbolType.set(TextUtils.equals(netType, "0") ? Utils.getString(R.string.module_asset_coin_type_test) : "");
        this.entity.set(entity);
        drawableImg = ContextCompat.getDrawable(viewModel.getApplication(), R.drawable.fragment_number_asset_bcx_icon);
        totalValue.set(TextUtils.equals(entity.symbol, "COCOS") ? CurrencyUtils.getSingleCurrencyType() + CurrencyUtils.getCocosPrice() : CurrencyUtils.getSingleCurrencyType() + "0.00");
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(5);
        nf.setGroupingUsed(false);
        frozenAmountViewVisible.set(new BigDecimal(entity.frozen_asset).compareTo(BigDecimal.ZERO) <= 0 ? View.GONE : View.VISIBLE);
        frozenAmount.set("冻结 " + entity.frozen_asset);
        BigDecimal usedAsset = entity.amount.subtract(new BigDecimal(entity.frozen_asset)).setScale(5, RoundingMode.HALF_UP).add(BigDecimal.ZERO);
        amount.set(nf.format(usedAsset));
    }
}





