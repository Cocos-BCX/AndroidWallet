package com.cocos.module_asset.ui.transfer;

import android.app.Application;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.AssetsModel;
import com.cocos.library_base.global.SPKeyGlobal;
import com.cocos.library_base.utils.SPUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.module_asset.R;

import java.math.BigDecimal;

/**
 * @author ningkang.guo
 * @Date 2019/2/18
 */
public class TransferViewModel extends BaseViewModel {


    public BigDecimal balance;
    private AssetsModel.AssetModel assetModel;

    public TransferViewModel(@NonNull Application application) {
        super(application);
        String netType = SPUtils.getString(Utils.getContext(), SPKeyGlobal.NET_TYPE, "");
        symbolType.set(TextUtils.equals(netType, "0") ? Utils.getString(R.string.module_asset_coin_type_test) : "");
    }

    //收款方帐户名的绑定
    public ObservableField<String> receivablesAccountName = new ObservableField<>();

    //帐户余额的绑定
    public ObservableField<String> accountBalance = new ObservableField<>();

    public ObservableField<String> symbolType = new ObservableField<>("");

    //转账数量的绑定
    public ObservableField<String> transferAmount = new ObservableField<>();

    //转账备注的绑定
    public ObservableField<String> transferMemo = new ObservableField<>("");

    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public void setTransferAssetModel(AssetsModel.AssetModel assetModel) {
        this.assetModel = assetModel;
    }


    public class UIChangeObservable {
        public ObservableBoolean transferBtnObservable = new ObservableBoolean(false);
        public ObservableBoolean toContact = new ObservableBoolean(false);
        public ObservableBoolean toCaptureActivity = new ObservableBoolean(false);
    }

    //返回按钮事件
    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    //扫描按钮
    public BindingCommand scanOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.toCaptureActivity.set(!uc.toCaptureActivity.get());
        }
    });

    //联系人按钮
    public BindingCommand contactOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.toContact.set(!uc.toContact.get());
        }
    });

    //全部按钮
    public BindingCommand transferAllOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            transferAmount.set(String.valueOf(balance));
        }
    });

    //转账按钮
    public BindingCommand transferNextOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.transferBtnObservable.set(!uc.transferBtnObservable.get());
        }
    });

    public void setAccountBalance(AssetsModel.AssetModel balance) {
        accountBalance.set(Utils.getString(R.string.module_asset_account_balance_text) + (balance.amount.compareTo(BigDecimal.ZERO) == 0 ? "0.00" : balance.amount.toString()) + balance.symbol);
    }
}
