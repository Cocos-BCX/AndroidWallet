package com.cocos.library_base.invokedpages.viewmodel;

import android.app.Application;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cocos.library_base.R;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.invokedpages.model.Contract;
import com.cocos.library_base.utils.Utils;

/**
 * @author ningkang.guo
 * @Date 2020/1/13
 */
public class InvokeContractViewModel extends BaseViewModel {

    private Contract contract;

    public InvokeContractViewModel(@NonNull Application application) {
        super(application);
    }

    //dapp icon url
    public ObservableField<String> invokeContractDappIconUrl = new ObservableField<>("");

    //dapp name
    public ObservableField<String> invokeContractDappName = new ObservableField<>("");

    public ObservableField<String> invokeContractNameAndAction = new ObservableField<>(Utils.getString(R.string.invoke_contract_defalut));

    public ObservableField<String> invokeContractDesc = new ObservableField<>("");

    //login account
    public ObservableField<String> invokeContractAccount = new ObservableField<>();


    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public ObservableBoolean invokeContractConfirmObservable = new ObservableBoolean(false);
        public ObservableBoolean invokeContractCancelObservable = new ObservableBoolean(false);
        public ObservableBoolean invokeContractDetailObservable = new ObservableBoolean(false);
    }


    //查看详情
    public BindingCommand invokeContractDetail = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.invokeContractDetailObservable.set(!uc.invokeContractDetailObservable.get());
        }
    });

    //调用合约
    public BindingCommand invokeContractConfirm = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.invokeContractConfirmObservable.set(!uc.invokeContractConfirmObservable.get());
        }
    });

    //取消调用
    public BindingCommand invokeContractCancel = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.invokeContractCancelObservable.set(!uc.invokeContractCancelObservable.get());
        }
    });


    public void setAuthorizeData(Contract contract) {
        this.contract = contract;
        if (!TextUtils.isEmpty(contract.getDappIcon())) {
            invokeContractDappIconUrl.set(contract.getDappIcon());
        }
        if (!TextUtils.isEmpty(contract.getDappName())) {
            invokeContractDappName.set(contract.getDappName());
        }
        if (!TextUtils.isEmpty(contract.getDesc())) {
            invokeContractDesc.set(contract.getDesc());
        }
        if (!TextUtils.isEmpty(contract.getContractNameOrId()) && !TextUtils.isEmpty(contract.getFunctionName())) {
            invokeContractNameAndAction.set(contract.getContractNameOrId() + ">" + contract.getFunctionName());
        }
        if (!TextUtils.isEmpty(contract.getAuthorizedAccount())) {
            invokeContractAccount.set(contract.getAuthorizedAccount());
        }
    }

}
