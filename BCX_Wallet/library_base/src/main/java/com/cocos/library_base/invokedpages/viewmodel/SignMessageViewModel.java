package com.cocos.library_base.invokedpages.viewmodel;

import android.app.Application;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.invokedpages.model.BaseInvokeModel;
import com.cocos.library_base.invokedpages.model.SignMessage;
import com.cocos.library_base.utils.AccountHelperUtils;

public class SignMessageViewModel extends BaseViewModel {

    public SignMessageViewModel(@NonNull Application application) {
        super(application);
    }

    //dapp icon url
    public ObservableField<String> invokeSignDappIconUrl = new ObservableField<>("");
    public ObservableField<String> invokeSignDappName = new ObservableField<>("");
    public ObservableField<String> invokeSignAccount = new ObservableField<>("");
    public ObservableField<String> signMessage = new ObservableField<>("");

    public UIChangeObservable uc = new UIChangeObservable();

    public void setSignData(SignMessage signMessageModel, BaseInvokeModel baseInvokeModel) {
        invokeSignAccount.set(AccountHelperUtils.getCurrentAccountName());
        if (null != signMessageModel) {
            signMessage.set(signMessageModel.getMessage());
        }
        if (null != baseInvokeModel) {
            invokeSignDappName.set(baseInvokeModel.getAppName());
            invokeSignDappIconUrl.set(signMessageModel.getDappIcon());
        }
    }

    public class UIChangeObservable {
        public ObservableBoolean invokeSignConfirm = new ObservableBoolean(false);
        public ObservableBoolean invokeSignCancel = new ObservableBoolean(false);
    }

    public BindingCommand invokeSignConfirm = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.invokeSignConfirm.set(!uc.invokeSignConfirm.get());
        }
    });

    public BindingCommand invokeSignCancel = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.invokeSignCancel.set(!uc.invokeSignCancel.get());
        }
    });
}
