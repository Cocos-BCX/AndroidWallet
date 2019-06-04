package com.cocos.module_asset.ui.transfer;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.bcx_sdk.bcx_log.LogUtils;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_asset.R;
import com.cocos.module_asset.entity.TransferModel;
import com.cocos.module_asset.entity.TransferParamsModel;

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
    public ObservableField<String> minerFee = new ObservableField<>();

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
            Log.i("getTransferAmount", transferParamsModel.getTransferAmount());
            Log.i("getPassword", transferParamsModel.getPassword());
            CocosBcxApiWrapper.getBcxInstance().transfer(transferParamsModel.getPassword(), transferParamsModel.getAccountName(), transferParamsModel.getReceivablesAccountName(),
                    transferParamsModel.getTransferAmount(), transferParamsModel.getTransferSymbol(), transferParamsModel.getFeeSymbol(), transferParamsModel.getTransferMemo(), new IBcxCallBack() {
                        @Override
                        public void onReceiveValue(final String s) {
                            MainHandler.getInstance().post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        TransferModel baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, TransferModel.class);
                                        if (!baseResult.isSuccess()) {
                                            return;
                                        }
                                        LogUtils.d("transfer", baseResult.data);
                                        ToastUtils.showShort(R.string.module_asset_transfer_success);
                                        EventBusCarrier eventBusCarrier = new EventBusCarrier();
                                        eventBusCarrier.setEventType(EventTypeGlobal.TRANSFER_SUCCESS);
                                        eventBusCarrier.setObject(null);
                                        EventBus.getDefault().post(eventBusCarrier);
                                    } catch (Exception e) {
                                        LogUtils.d("transferhandleException", e.getMessage());
                                    }
                                }
                            });
                        }
                    });
        }
    });

    public void setTransferInfoData(TransferParamsModel transferParamsModel) {
        this.transferParamsModel = transferParamsModel;
        transferAccount.set(transferParamsModel.getAccountName());
        receivablesAccount.set(transferParamsModel.getReceivablesAccountName());
        orderAmount.set(transferParamsModel.getTransferAmount() + transferParamsModel.getTransferSymbol());
        orderMemo.set(transferParamsModel.getTransferMemo());
        minerFee.set(transferParamsModel.getFee() + transferParamsModel.getFeeSymbol());
    }

}
