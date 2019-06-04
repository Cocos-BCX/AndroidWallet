package com.cocos.module_login.backup;

import android.databinding.Observable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.entity.PrivateKeyModel;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_login.BR;
import com.cocos.module_login.R;
import com.cocos.module_login.databinding.ActivityBackupWalletBinding;

/**
 * @author ningkang.guo
 * @Date 2019/2/1
 */
@Route(path = RouterActivityPath.ACTIVITY_BACKUP_WALLET)
public class BackupWalletActivity extends BaseActivity<ActivityBackupWalletBinding, BackupWalletViewModel> {

    private String password;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_backup_wallet;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        try {
            password = getIntent().getExtras().getString(IntentKeyGlobal.ACCOUNT_PASSWORD);
        } catch (Exception e) {
        }
    }

    @Override
    public void initData() {
        CocosBcxApiWrapper.getBcxInstance().export_private_key(AccountHelperUtils.getCurrentAccountName(), password, new IBcxCallBack() {
            @Override
            public void onReceiveValue(final String private_key) {
                Log.d("export_private_key", private_key);
                MainHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        final PrivateKeyModel keyModel = GsonSingleInstance.getGsonInstance().fromJson(private_key, PrivateKeyModel.class);
                        if (!keyModel.isSuccess()) {
                            return;
                        }
                        viewModel.setPrivateKeyModel(keyModel);
                    }
                });
            }
        });
    }
}
