package com.cocos.module_asset.nh_order_manager;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.entity.NhAssetOrderEntity;
import com.cocos.library_base.global.EventTypeGlobal;

import org.greenrobot.eventbus.EventBus;

/**
 * @author ningkang.guo
 * @Date 2019/7/19
 */
public class BuyOrderConfirmViewModel extends BaseViewModel {

    public ObservableField<String> orderPrice = new ObservableField<>("");
    public ObservableField<String> minerFee = new ObservableField<>("");
    public ObservableField<String> orderId = new ObservableField<>("");
    public ObservableField<String> nhAssetId = new ObservableField<>("");
    public NhAssetOrderEntity.NhOrderBean nhOrderBean;

    public BuyOrderConfirmViewModel(@NonNull Application application) {
        super(application);
    }

    //取消订单按钮
    public BindingCommand buyConfirmOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.SHOW_BUY_ORDER_PASSWORD_VERIFY_DIALOG);
            eventBusCarrier.setObject(nhOrderBean);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });

    public void setBuyOrderModel(NhAssetOrderEntity.NhOrderBean nhOrderBean) {
        this.nhOrderBean = nhOrderBean;
        orderId.set(nhOrderBean.id);
        orderPrice.set(nhOrderBean.priceWithSymbol);
        minerFee.set(nhOrderBean.minerFee + " " + nhOrderBean.feeSymbol);
        nhAssetId.set(nhOrderBean.nh_asset_id);
    }

    public BindingCommand dismissOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.DIALOG_DISMISS_TYPE);
            eventBusCarrier.setObject(nhOrderBean);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });
}
