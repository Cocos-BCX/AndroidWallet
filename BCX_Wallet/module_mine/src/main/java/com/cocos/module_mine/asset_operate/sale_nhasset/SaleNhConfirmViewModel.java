package com.cocos.module_mine.asset_operate.sale_nhasset;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.module_mine.entity.SaleNHAssetParamsModel;

import org.greenrobot.eventbus.EventBus;

/**
 * @author ningkang.guo
 * @Date 2019/2/20
 */
public class SaleNhConfirmViewModel extends BaseViewModel {


    public SaleNhConfirmViewModel(@NonNull Application application) {
        super(application);
    }

    private SaleNHAssetParamsModel saleAssetParamsModel;
    public ObservableField<String> nhAssetId = new ObservableField<>("");
    public ObservableField<String> priceAmount = new ObservableField<>("");
    public ObservableField<String> validTime = new ObservableField<>("");
    public ObservableField<String> orderMemo = new ObservableField<>("");

    // 关闭弹窗
    public BindingCommand dismissOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.DIALOG_DISMISS_TYPE);
            eventBusCarrier.setObject(null);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });

    //出售按钮的点击事件
    public BindingCommand saleConfirmOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.SHOW_SALE_NH_ASSET_PASSWORD_VERIFY_DIALOG);
            eventBusCarrier.setObject(saleAssetParamsModel);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });


    public void setSaleInfoData(SaleNHAssetParamsModel saleNHAssetParamsModel) {
        this.saleAssetParamsModel = saleNHAssetParamsModel;
        nhAssetId.set(saleNHAssetParamsModel.getNhAssetId());
        priceAmount.set(saleNHAssetParamsModel.getPriceAmount() + " " + saleNHAssetParamsModel.getPriceSymbol());
        validTime.set(saleNHAssetParamsModel.getValidTime() + " s");
        orderMemo.set(saleNHAssetParamsModel.getOrderMemo());
    }
}
