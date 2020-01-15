package com.cocos.library_base.invokedpages.viewmodel;

import android.app.Application;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.invokedpages.model.Authorize;
import com.cocos.library_base.invokedpages.model.BaseInvokeModel;
import com.cocos.library_base.utils.AccountHelperUtils;

/**
 * @author ningkang.guo
 * @Date 2020/1/13
 */
public class InvokeLoginViewModel extends BaseViewModel {

    private Authorize authorize;

    public InvokeLoginViewModel(@NonNull Application application) {
        super(application);
    }

    //dapp icon url
    public ObservableField<String> invokeLoginDappIconUrl = new ObservableField<>("");

    //dapp name
    public ObservableField<String> invokeLoginDappName = new ObservableField<>("");

    //dapp desc
    public ObservableField<String> invokeLoginDappDesc = new ObservableField<>("");

    //login account
    public ObservableField<String> invokeLoginAccount = new ObservableField<>(AccountHelperUtils.getCurrentAccountName());


    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public ObservableBoolean invokeLoginSwitchObservable = new ObservableBoolean(false);
        public ObservableBoolean invokeLoginConfirmObservable = new ObservableBoolean(false);
        public ObservableBoolean invokeLoginCancelObservable = new ObservableBoolean(false);
    }


    //切换账号
    public BindingCommand invokeLoginSwitch = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.invokeLoginSwitchObservable.set(!uc.invokeLoginSwitchObservable.get());
        }
    });

    //确认登录请求
    public BindingCommand invokeLoginConfirm = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.invokeLoginConfirmObservable.set(!uc.invokeLoginConfirmObservable.get());
        }
    });

    //取消登录请求
    public BindingCommand invokeLoginCancel = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.invokeLoginCancelObservable.set(!uc.invokeLoginCancelObservable.get());
        }
    });

    public void setAuthorizeData(Authorize authorize) {
        this.authorize = authorize;
        if (!TextUtils.isEmpty(authorize.getDappIcon())) {
            invokeLoginDappIconUrl.set(authorize.getDappIcon());
        }
        if (!TextUtils.isEmpty(authorize.getDappName())) {
            invokeLoginDappName.set(authorize.getDappName());
        }
        if (!TextUtils.isEmpty(authorize.getDesc())) {
            invokeLoginDappDesc.set(authorize.getDesc());
        }
    }

}
