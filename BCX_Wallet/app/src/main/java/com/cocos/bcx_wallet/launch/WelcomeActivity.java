package com.cocos.bcx_wallet.launch;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.cocos.bcx_wallet.BCXApplication;
import com.cocos.bcx_wallet.BR;
import com.cocos.bcx_wallet.R;
import com.cocos.bcx_wallet.databinding.ActivityWelcomeBinding;
import com.cocos.bcx_wallet.main.MainActivity;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.entity.NodeInfoModel;
import com.cocos.library_base.global.SPKeyGlobal;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.SPUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.module_login.password_login.PasswordLoginActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/**
 * @author ningkang.guo
 * @Date 2019/1/29
 */
public class WelcomeActivity extends BaseActivity<ActivityWelcomeBinding, BaseViewModel> {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_welcome;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }


    @SuppressLint("CheckResult")
    @Override
    public void initData() {
        RxPermissions rxPermissions = new RxPermissions(WelcomeActivity.this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) {
                        NodeInfoModel.DataBean selectedNodeModel = SPUtils.getObject(Utils.getContext(), SPKeyGlobal.NODE_WORK_MODEL_SELECTED);
                        boolean is_first_connect = SPUtils.getBoolean(WelcomeActivity.this, SPKeyGlobal.IS_FIRST_CONNECT, true);
                        if (null != selectedNodeModel && is_first_connect) {
                            selectedNodeModel = null;
                            AccountHelperUtils.setCurrentAccountName("");
                            SPUtils.putBoolean(WelcomeActivity.this, SPKeyGlobal.IS_FIRST_CONNECT, false);
                        }
                        if (!TextUtils.isEmpty(AccountHelperUtils.getCurrentAccountName())) {
                            startActivity(MainActivity.class);
                            finish();
                            return;
                        }
                        startActivity(PasswordLoginActivity.class);
                        finish();
                    }
                });
    }

}
