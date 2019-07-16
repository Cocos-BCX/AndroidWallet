package com.cocos.module_asset.nh_order_manager;

import android.annotation.SuppressLint;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.cocos.library_base.base.ItemViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.NhAssetOrderEntity;
import com.cocos.library_base.utils.TimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ningkang.guo
 * @Date 2019/7/15
 */
public class MineNhOrderItemViewModel extends ItemViewModel {


    public ObservableField<String> mineNhOrderId = new ObservableField<>("");
    public ObservableField<String> mineNhOrderPrice = new ObservableField<>("");
    public ObservableField<String> nhAssetIconUrl = new ObservableField<>("http://47.75.186.60/itemimgs/bomb.png");
    public ObservableField<String> nhAssetId = new ObservableField<>("");
    public ObservableField<String> mineNhOrderExpritationTime = new ObservableField<>("");
    public ObservableField<String> mineNhOrderMemo = new ObservableField<>("");
    NhAssetOrderEntity.NhOrderBean entity;

    public MineNhOrderItemViewModel(@NonNull MineNhOrderViewModel viewModel, NhAssetOrderEntity.NhOrderBean nhOrderEntity) {
        super(viewModel);
        this.entity = nhOrderEntity;
        mineNhOrderId.set(nhOrderEntity.id);
        mineNhOrderPrice.set(nhOrderEntity.priceWithSymbol);
        nhAssetId.set(nhOrderEntity.nh_asset_id);
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        Date dateObject = null;
        try {
            dateObject = sDateFormat.parse(nhOrderEntity.expiration);
            mineNhOrderExpritationTime.set("过期时间：" + TimeUtil.formDate(dateObject));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mineNhOrderMemo.set(nhOrderEntity.memo);
    }

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });

    //取消订单按钮
    public BindingCommand cancelNhOrderCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });
}
