package com.cocos.module_asset.nh_order_manager;

import android.annotation.SuppressLint;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.library_base.base.ItemViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.entity.NhAssetOrderEntity;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.TimeUtil;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ningkang.guo
 * @Date 2019/7/15
 */
public class AllNhOrderItemViewModel extends ItemViewModel {


    public ObservableField<String> allNhOrderId = new ObservableField<>("");
    public ObservableField<String> allNhOrderPrice = new ObservableField<>("");
    public ObservableField<String> nhAssetIconUrl = new ObservableField<>("http://47.75.186.60/itemimgs/bomb.png");
    public ObservableField<String> allNhOrderSeller = new ObservableField<>("");
    public ObservableField<String> allNhOrderExpritationTime = new ObservableField<>("");
    public ObservableField<String> allNhOrderMemo = new ObservableField<>("");
    NhAssetOrderEntity.NhOrderBean entity;

    public AllNhOrderItemViewModel(@NonNull AllNhOrderViewModel viewModel, NhAssetOrderEntity.NhOrderBean nhOrderEntity) {
        super(viewModel);
        this.entity = nhOrderEntity;
        allNhOrderId.set(entity.id);
        allNhOrderPrice.set(entity.priceWithSymbol);
        String seller = CocosBcxApiWrapper.getBcxInstance().get_account_name_by_id(entity.seller);
        allNhOrderSeller.set(seller);
        entity.sellerName = seller;
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        Date dateObject = null;
        try {
            dateObject = sDateFormat.parse(nhOrderEntity.expiration);
            entity.expirationTime = TimeUtil.formDate(dateObject);
            allNhOrderExpritationTime.set("过期时间：" + entity.expirationTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        allNhOrderMemo.set(entity.memo);
    }


    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            entity.isMineOrder = false;
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKeyGlobal.NH_ORDER_MODEL, entity);
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_NH_ORDER_DETAIL).with(bundle).navigation();
        }
    });

    //购买订单按钮
    public BindingCommand buyNhOrderCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.SHOW_PASSWORD_VERIFY_DIALOG);
            eventBusCarrier.setObject(entity);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });
}
