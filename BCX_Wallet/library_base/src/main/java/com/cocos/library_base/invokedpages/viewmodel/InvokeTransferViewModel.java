package com.cocos.library_base.invokedpages.viewmodel;

import android.app.Application;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.invokedpages.model.BaseInvokeModel;
import com.cocos.library_base.invokedpages.model.Transfer;
import com.cocos.library_base.utils.AccountHelperUtils;

import java.text.NumberFormat;

/**
 * @author ningkang.guo
 * @Date 2020/1/13
 */
public class InvokeTransferViewModel extends BaseViewModel {

    private Transfer transfer;
    private BaseInvokeModel baseInfo;

    public InvokeTransferViewModel(@NonNull Application application) {
        super(application);
    }

    //dapp icon url
    public ObservableField<String> invokeTransferDappIconUrl = new ObservableField<>("");

    //dapp name
    public ObservableField<String> invokeTransferDappName = new ObservableField<>("");

    //dapp desc
    public ObservableField<String> invokeTransferAmount = new ObservableField<>("");
    public ObservableField<String> invokeTransferActionId = new ObservableField<>("");
    public ObservableField<String> invokeTransferMemo = new ObservableField<>("");
    public ObservableField<String> invokeTransferDesc = new ObservableField<>("");

    //login account
    public ObservableField<String> invokeTransferFrom = new ObservableField<>(AccountHelperUtils.getCurrentAccountName());
    public ObservableField<String> invokeTransferTo = new ObservableField<>(AccountHelperUtils.getCurrentAccountName());


    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public ObservableBoolean invokeTransferConfirmObservable = new ObservableBoolean(false);
        public ObservableBoolean invokeTransferCancelObservable = new ObservableBoolean(false);
    }


    //切换账号
    public BindingCommand invokeTransferConfirm = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.invokeTransferConfirmObservable.set(!uc.invokeTransferConfirmObservable.get());
        }
    });

    //切换账号
    public BindingCommand invokeTransferCancel = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.invokeTransferCancelObservable.set(!uc.invokeTransferCancelObservable.get());
        }
    });


    public void setTransferData(Transfer transfer, BaseInvokeModel baseInfo) {
        this.transfer = transfer;
        this.baseInfo = baseInfo;
        if (!TextUtils.isEmpty(transfer.getDappIcon())) {
            invokeTransferDappIconUrl.set(transfer.getDappIcon());
        }
        if (!TextUtils.isEmpty(transfer.getMemo())) {
            invokeTransferMemo.set(transfer.getMemo());
        }
        if (!TextUtils.isEmpty(transfer.getDappName())) {
            invokeTransferDappName.set(transfer.getDappName());
        }
        if (!TextUtils.isEmpty(transfer.getDesc())) {
            invokeTransferDesc.set(transfer.getDesc());
        }
        if (!TextUtils.isEmpty(String.valueOf(transfer.getAmount()))) {
            NumberFormat instance = NumberFormat.getInstance();
            instance.setGroupingUsed(false);
            instance.setMaximumFractionDigits(transfer.getPrecision());
            invokeTransferAmount.set(instance.format(transfer.getAmount()) + transfer.getSymbol());
        }
        if (!TextUtils.isEmpty(transfer.getFrom())) {
            invokeTransferFrom.set(transfer.getFrom());
        }
        if (!TextUtils.isEmpty(transfer.getTo())) {
            invokeTransferTo.set(transfer.getTo());
        }
    }

}
