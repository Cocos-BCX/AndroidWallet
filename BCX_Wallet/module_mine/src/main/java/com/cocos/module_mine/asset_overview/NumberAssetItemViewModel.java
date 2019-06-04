package com.cocos.module_mine.asset_overview;

import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.cocos.library_base.base.ItemViewModel;
import com.cocos.library_base.entity.AssetsModel;
import com.cocos.library_base.utils.Utils;
import com.cocos.module_mine.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author ningkang.guo
 * @Date 2019/2/25
 */
public class NumberAssetItemViewModel extends ItemViewModel {

    public ObservableField<AssetsModel.AssetModel> entity = new ObservableField<>();
    public Drawable drawableImg;
    public ObservableField<String> totalValue = new ObservableField<>("≈ ￥0.00");
    public ObservableField<String> symbolType = new ObservableField<>(Utils.getString(R.string.module_asset_coin_type_test));
    public ObservableField<String> amount = new ObservableField<>("0.00");

    public NumberAssetItemViewModel(@NonNull NumberAssetViewModel viewModel, AssetsModel.AssetModel entity) {
        super(viewModel);
        this.entity.set(entity);
        drawableImg = ContextCompat.getDrawable(viewModel.getApplication(), R.drawable.fragment_number_asset_bcx_icon);
        totalValue.set("≈ ￥" + String.valueOf(entity.amount.multiply(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP)));
        amount.set(String.valueOf(entity.amount.add(BigDecimal.ZERO)));
    }
/*
    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_DEAL_RECORD).navigation();
        }
    });*/
}





