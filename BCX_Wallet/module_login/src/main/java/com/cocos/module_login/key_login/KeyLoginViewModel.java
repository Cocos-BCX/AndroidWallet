package com.cocos.module_login.key_login;

import android.annotation.SuppressLint;
import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.bcx_sdk.bcx_entity.AccountType;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.KeyLoginModel;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.RegexUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_login.R;


/**
 * @author ningkang.guo
 * @Date 2019/1/28
 */
public class KeyLoginViewModel extends BaseViewModel {

    private int from;

    //密码的绑定
    public ObservableField<String> password = new ObservableField<>();

    //私钥的绑定
    public ObservableField<String> privateKey = new ObservableField<>();


    public KeyLoginViewModel(@NonNull Application application) {
        super(application);
    }

    //返回按钮的点击事件
    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (from == 1) {
                ARouter.getInstance().build(RouterActivityPath.ACTIVITY_REGISTER).navigation();
            } else if (from == 2) {
                ARouter.getInstance().build(RouterActivityPath.ACTIVITY_PASSWORD_LOGIN).navigation();
            }
            finish();
        }
    });

    //登录按钮的点击事件
    public BindingCommand keyLoginOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            keyLogin();
        }
    });


    //登录按钮的点击事件
    public BindingCommand loginClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_PASSWORD_LOGIN).navigation();
        }
    });


    /**
     * 私钥登陆
     **/
    @SuppressLint("CheckResult")
    private void keyLogin() {
        if (TextUtils.isEmpty(privateKey.get())) {
            ToastUtils.showShort(R.string.module_login_private_key_empty);
            return;
        }
        if (TextUtils.isEmpty(password.get())) {
            ToastUtils.showShort(R.string.module_login_temp_password_empty);
            return;
        }

        if (!RegexUtils.isLegalKey(privateKey.get())) {
            ToastUtils.showShort(R.string.module_login_key_format_error);
            return;
        }
        showDialog();
        try {
            CocosBcxApiWrapper.getBcxInstance().import_wif_key(privateKey.get(), password.get(), AccountType.WALLET.name(),
                    new IBcxCallBack() {
                        @Override
                        public void onReceiveValue(final String s) {
                            MainHandler.getInstance().post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i("import_wif_key", s);
                                    KeyLoginModel keyLoginModel = GsonSingleInstance.getGsonInstance().fromJson(s, KeyLoginModel.class);
                                    if (keyLoginModel.code == 109 || keyLoginModel.code == 1011 || keyLoginModel.code == 135) {
                                        ToastUtils.showShort(R.string.module_login_key_format_error);
                                        dismissDialog();
                                        return;
                                    }
                                    if (keyLoginModel.code == 110) {
                                        ToastUtils.showShort(R.string.module_login_private_key_no_account_info);
                                        dismissDialog();
                                        return;
                                    }

                                    if (!keyLoginModel.isSuccess()) {
                                        ToastUtils.showShort(R.string.net_work_failed);
                                        dismissDialog();
                                        return;
                                    }
                                    AccountHelperUtils.setCurrentAccountName(keyLoginModel.data.get(0));
                                    ARouter.getInstance().build(RouterActivityPath.ACTIVITY_MAIN_PATH).navigation();
                                    ToastUtils.showShort(R.string.module_login_key_login_success);
                                    finish();
                                    dismissDialog();
                                }
                            });
                        }
                    });
        } catch (Exception e) {
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setFrom(int from) {
        this.from = from;
    }
}
