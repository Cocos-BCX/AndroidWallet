package com.cocos.module_mine.export_private;

import android.app.Application;
import android.content.ClipData;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.PrivateKeyModel;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.singleton.ClipboardManagerInstance;
import com.cocos.module_mine.R;

import java.util.Map;


/**
 * @author ningkang.guo
 * @Date 2019/2/22
 */
public class ExportKeyViewModel extends BaseViewModel {

    public ExportKeyViewModel(@NonNull Application application) {
        super(application);
    }

    //资产私钥的绑定
    public ObservableField<String> assetPrivateKey = new ObservableField<>("");

    //账户私钥的绑定
    public ObservableField<String> accountPrivateKey = new ObservableField<>("");
    public ObservableInt activityPrivateKeyVisible = new ObservableInt(View.GONE);
    public ObservableInt ownerPrivateKeyVisible = new ObservableInt(View.GONE);

    //复制按钮的点击事件
    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    //复制账户私钥按钮的点击事件
    public BindingCommand accountPrivateKeyCopyOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (TextUtils.isEmpty(accountPrivateKey.get())) {
                return;
            }
            ClipData mClipData = ClipData.newPlainText("Label", accountPrivateKey.get());
            ClipboardManagerInstance.getClipboardManager().setPrimaryClip(mClipData);
            ToastUtils.showShort(R.string.copy_success);
        }
    });

    //复制资产私钥按钮的点击事件
    public BindingCommand assetPrivateKeyCopyOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (TextUtils.isEmpty(assetPrivateKey.get())) {
                return;
            }
            ClipData mClipData = ClipData.newPlainText("Label", assetPrivateKey.get());
            ClipboardManagerInstance.getClipboardManager().setPrimaryClip(mClipData);
            ToastUtils.showShort(R.string.copy_success);
        }
    });

    public void setPrivateKeyModel(PrivateKeyModel privateKeyModel) {
        Map<String, String> keys = privateKeyModel.getData();
        for (Map.Entry<String, String> public_keys : keys.entrySet()) {
            if (TextUtils.equals(public_keys.getKey(), AccountHelperUtils.getCurrentActivePublicKey())) {
                assetPrivateKey.set(public_keys.getValue());
                activityPrivateKeyVisible.set(View.VISIBLE);
            } else {
                accountPrivateKey.set(public_keys.getValue());
                ownerPrivateKeyVisible.set(View.VISIBLE);
            }
        }

    }
}
