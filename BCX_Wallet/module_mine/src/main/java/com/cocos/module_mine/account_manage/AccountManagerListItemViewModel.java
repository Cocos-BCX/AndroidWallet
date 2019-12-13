package com.cocos.module_mine.account_manage;

import android.content.ClipData;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.bcx_sdk.bcx_entity.AccountEntity;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.base.ItemViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.AssetsModel;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.global.SPKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.SPUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.singleton.ClipboardManagerInstance;
import com.cocos.module_mine.R;

import java.math.BigDecimal;

/**
 * @author ningkang.guo
 * @Date 2019/4/8
 */
public class AccountManagerListItemViewModel extends ItemViewModel {

    public ObservableField<AssetsModel.AssetModel> entity = new ObservableField<>();

    public ObservableInt current_account_visible = new ObservableInt(View.INVISIBLE);

    public ObservableField<String> account = new ObservableField<>("");

    public ObservableField<String> amount = new ObservableField<>("");

    public ObservableField<String> symbolType = new ObservableField<>("");

    AccountEntity.AccountBean daoAccount;

    public AccountManagerListItemViewModel(@NonNull BaseViewModel viewModel, AssetsModel.AssetModel entity, AccountEntity.AccountBean daoAccount) {
        super(viewModel);
        this.daoAccount = daoAccount;
        this.entity.set(entity);
        account.set(daoAccount.getName());
        amount.set(String.valueOf(entity.amount.add(BigDecimal.ZERO)));
        current_account_visible.set(TextUtils.equals(AccountHelperUtils.getCurrentAccountName(), daoAccount.getName()) ? View.VISIBLE : View.INVISIBLE);
        String netType = SPUtils.getString(Utils.getContext(), SPKeyGlobal.NET_TYPE, "");
        symbolType.set(TextUtils.equals(netType, "0") ? Utils.getString(R.string.module_asset_coin_type_test) : "");
    }


    public BindingCommand copy = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (TextUtils.isEmpty(account.get())) {
                return;
            }
            ClipData mClipData = ClipData.newPlainText("Label", account.get());
            ClipboardManagerInstance.getClipboardManager().setPrimaryClip(mClipData);
            ToastUtils.showShort(R.string.copy_success);
        }
    });


    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKeyGlobal.DAO_ACCOUNT_MODEL, daoAccount);
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_ACCOUNT_MANAGE).with(bundle).navigation();
        }
    });
}
