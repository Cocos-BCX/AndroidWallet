package com.cocos.bcx_wallet.launch;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;

import com.cocos.bcx_wallet.BR;
import com.cocos.bcx_wallet.R;
import com.cocos.bcx_wallet.databinding.ActivityWelcomeBinding;
import com.cocos.bcx_wallet.main.MainActivity;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.base.BaseViewModel;
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
                        startActivity(MainActivity.class);
                        finish();
                    }
                });
    }

}
