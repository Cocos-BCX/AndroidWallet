package com.cocos.module_mine.asset_operate.transfer_nhasset;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.module_mine.entity.NHAssetModel;

import org.greenrobot.eventbus.EventBus;

/**
 * @author ningkang.guo
 * @Date 2019/7/22
 */
public class TransferNhAssetConfirmViewModel extends BaseViewModel {


    public ObservableField<String> from = new ObservableField<>("");
    public ObservableField<String> to = new ObservableField<>("");
    public ObservableField<String> nhAssetId = new ObservableField<>("");
    public ObservableField<String> minerFee = new ObservableField<>("");

    NHAssetModel.NHAssetModelBean nHAssetModelBean;

    public TransferNhAssetConfirmViewModel(@NonNull Application application) {
        super(application);
    }

    //转移资产按钮
    public BindingCommand transferConfirmOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.SHOW_TRANSFER_NH_ASSET_PASSWORD_VERIFY_DIALOG);
            eventBusCarrier.setObject(nHAssetModelBean);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });


    public BindingCommand dismissOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.DIALOG_DISMISS_TYPE);
            eventBusCarrier.setObject(nHAssetModelBean);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });

    public void setNhAssetModel(NHAssetModel.NHAssetModelBean nHAssetModelBean) {
        this.nHAssetModelBean = nHAssetModelBean;
        from.set(nHAssetModelBean.from);
        to.set(nHAssetModelBean.to);
        nhAssetId.set(nHAssetModelBean.id);
        minerFee.set(nHAssetModelBean.minerFee + " " + nHAssetModelBean.feeSymbol);

    }
}
