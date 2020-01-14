package com.cocos.library_base.component.transfer;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.cocos.library_base.R;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.entity.TransferParamsModel;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.utils.Utils;

import org.greenrobot.eventbus.EventBus;


/**
 * @author ningkang.guo
 * @Date 2019/2/20
 */
public class OrderConfirmViewModel extends BaseViewModel {

    private TransferParamsModel transferParamsModel;

    public OrderConfirmViewModel(@NonNull Application application) {
        super(application);
    }

    public ObservableField<String> orderType = new ObservableField<>(Utils.getString(R.string.module_asset_transfer_title));
    public ObservableField<String> transferAccount = new ObservableField<>();
    public ObservableField<String> receivablesAccount = new ObservableField<>();
    public ObservableField<String> orderAmount = new ObservableField<>();
    public ObservableField<String> orderMemo = new ObservableField<>("");

    //转账按钮的点击事件
    public BindingCommand dismissOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.DIALOG_DISMISS_TYPE);
            eventBusCarrier.setObject(null);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });

    //转账按钮的点击事件
    public BindingCommand payConfirmOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.SHOW_TRANSFER_PASSWORD_VERIFY_DIALOG);
            eventBusCarrier.setObject(transferParamsModel);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });

    public void setTransferInfoData(TransferParamsModel transferParamsModel) {
        this.transferParamsModel = transferParamsModel;
        transferAccount.set(transferParamsModel.getAccountName());
        receivablesAccount.set(transferParamsModel.getReceivablesAccountName());
        orderAmount.set(transferParamsModel.getTransferAmount() + transferParamsModel.getTransferSymbol());
        orderMemo.set(transferParamsModel.getTransferMemo());
    }

}
