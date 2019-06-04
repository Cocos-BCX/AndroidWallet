package com.cocos.module_mine.account_manage;

import android.content.ClipData;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.base.ItemViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.AssetsModel;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
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

    public ObservableField<String> symbolType = new ObservableField<>(Utils.getString(R.string.module_asset_coin_type_test));

    public AccountManagerListItemViewModel(@NonNull BaseViewModel viewModel, AssetsModel.AssetModel entity, String accountName) {
        super(viewModel);
        this.entity.set(entity);
        account.set(accountName);
        amount.set(String.valueOf(entity.amount.add(BigDecimal.ZERO)));
        current_account_visible.set(TextUtils.equals(AccountHelperUtils.getCurrentAccountName(), accountName) ? View.VISIBLE : View.INVISIBLE);
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
            bundle.putString(IntentKeyGlobal.ACCOUNT_NAME, account.get());
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_ACCOUNT_MANAGE).with(bundle).navigation();
        }
    });
}
