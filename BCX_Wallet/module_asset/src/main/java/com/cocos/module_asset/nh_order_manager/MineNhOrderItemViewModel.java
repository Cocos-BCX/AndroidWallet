package com.cocos.module_asset.nh_order_manager;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.library_base.base.ItemViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.entity.NhAssetOrderEntity;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;

import org.greenrobot.eventbus.EventBus;

/**
 * @author ningkang.guo
 * @Date 2019/7/15
 */
public class MineNhOrderItemViewModel extends ItemViewModel {


    public ObservableField<String> mineNhOrderId = new ObservableField<>("");
    public ObservableField<String> mineNhOrderPrice = new ObservableField<>("");
    public ObservableField<String> seller = new ObservableField<>("");
    public ObservableField<String> mineNhOrderExpritationTime = new ObservableField<>("");
    public ObservableField<String> mineNhOrderMemo = new ObservableField<>("");
    NhAssetOrderEntity.NhOrderBean entity;

    public MineNhOrderItemViewModel(@NonNull MineNhOrderViewModel viewModel, NhAssetOrderEntity.NhOrderBean nhOrderEntity) {
        super(viewModel);
        this.entity = nhOrderEntity;
        mineNhOrderId.set(nhOrderEntity.id);
        mineNhOrderPrice.set(nhOrderEntity.priceWithSymbol);
        seller.set(nhOrderEntity.sellerName);
        mineNhOrderExpritationTime.set("过期时间：" + entity.expirationTime);
        mineNhOrderMemo.set(nhOrderEntity.memo);
    }

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            entity.isMineOrder = true;
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKeyGlobal.NH_ORDER_MODEL, entity);
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_NH_ORDER_DETAIL).with(bundle).navigation();
        }
    });


    //取消订单按钮
    public BindingCommand cancelNhOrderCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.SHOW_CANCEL_ORDER_CONFIRM_DIALOG);
            eventBusCarrier.setObject(entity);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });
}
