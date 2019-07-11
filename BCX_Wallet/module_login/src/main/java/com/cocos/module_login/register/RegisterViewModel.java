package com.cocos.module_login.register;

import android.app.Application;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.bcx_sdk.bcx_entity.AccountType;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.binding.command.BindingConsumer;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.RegexUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_login.R;
import com.cocos.module_login.entity.RegisterModel;


/**
 * @author ningkang.guo
 * @Date 2019/1/28
 */
public class RegisterViewModel extends BaseViewModel {

    // 必须设置默认值
    private AccountType accountType = AccountType.ACCOUNT;
    //用户名的绑定
    public ObservableField<String> accountName = new ObservableField<>();
    //密码的绑定
    public ObservableField<String> password = new ObservableField<>();
    //确认密码的绑定
    public ObservableField<String> confirmPassword = new ObservableField<>();
    //注册提示文字的绑定
    public ObservableField<String> registerModelTips = new ObservableField<>(Utils.getString(R.string.module_login_account_model_tips));

    public ObservableInt walletModelTitleColor = new ObservableInt(Utils.getColor(R.color.color_A5A9B1));

    public ObservableInt accountModelTitleColor = new ObservableInt(Utils.getColor(R.color.color_262A33));

    public ObservableInt accountNameErrorTipVisible = new ObservableInt(View.INVISIBLE);

    public RegisterViewModel(@NonNull Application application) {
        super(application);
    }

    //导入按钮的点击事件
    public BindingCommand importOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle = new Bundle();
            bundle.putInt(IntentKeyGlobal.FROM, 1);
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_KEYLOGIN).with(bundle).navigation();
        }
    });

    //密码登录按钮的点击事件
    public BindingCommand accountLoginClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_PASSWORD_LOGIN).navigation();
        }
    });


    //用户名输入框焦点改变的回调事件
    public BindingCommand<Boolean> onFocusChangeCommand = new BindingCommand<>(new BindingConsumer<Boolean>() {
        @Override
        public void call(Boolean hasFocus) {
            if (!hasFocus && !TextUtils.isEmpty(accountName.get())) {
                if (!RegexUtils.isAccountName(accountName.get())) {
                    accountNameErrorTipVisible.set(View.VISIBLE);
                } else {
                    accountNameErrorTipVisible.set(View.INVISIBLE);
                }
            }
        }
    });


    //钱包类型点击事件
    public BindingCommand walletTitleOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            walletModelTitleColor.set(Utils.getColor(R.color.color_262A33));
            accountModelTitleColor.set(Utils.getColor(R.color.color_A5A9B1));
            registerModelTips.set(Utils.getString(R.string.module_login_wallet_model_tips));
            accountType = AccountType.WALLET;
        }
    });

    //账户类型点击事件
    public BindingCommand accountOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            walletModelTitleColor.set(Utils.getColor(R.color.color_A5A9B1));
            accountModelTitleColor.set(Utils.getColor(R.color.color_262A33));
            registerModelTips.set(Utils.getString(R.string.module_login_account_model_tips));
            accountType = AccountType.ACCOUNT;
        }
    });


    //注册按钮的点击事件
    public BindingCommand registerOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            register(accountType);
        }
    });

    private void register(AccountType accountType) {
        if (TextUtils.isEmpty(accountName.get())) {
            ToastUtils.showShort(R.string.module_login_account_name_hint);
            return;
        }
        if (!RegexUtils.isAccountName(accountName.get())) {
            accountNameErrorTipVisible.set(View.VISIBLE);
            return;
        }
        if (TextUtils.isEmpty(password.get())) {
            ToastUtils.showShort(R.string.module_login_password_empty);
            return;
        }
        if (TextUtils.isEmpty(confirmPassword.get())) {
            ToastUtils.showShort(R.string.module_login_password_confirm_empty);
            return;
        }
        if (!TextUtils.equals(password.get(), confirmPassword.get())) {
            ToastUtils.showShort(R.string.module_login_password_confirm_failure);
            return;
        }
        if (!RegexUtils.isLegalPassword(password.get())) {
            ToastUtils.showShort(R.string.module_login_password_illegal);
            return;
        }

        showDialog();

        CocosBcxApiWrapper.getBcxInstance().create_account(accountName.get(), password.get(), accountType, true, new IBcxCallBack() {
            @Override
            public void onReceiveValue(final String s) {
                Log.d("create_account", s);
                MainHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            RegisterModel registerModel = GsonSingleInstance.getGsonInstance().fromJson(s, RegisterModel.class);

                            if (registerModel.code == 0) {
                                ToastUtils.showShort(com.cocos.library_base.R.string.net_work_failed);
                                return;
                            }

                            if (registerModel.code == 159) {
                                ToastUtils.showShort(R.string.module_login_account_exist);
                                dismissDialog();
                                return;
                            }

                            if (!registerModel.isSuccess()) {
                                dismissDialog();
                                return;
                            }
                            AccountHelperUtils.setCurrentAccountName(registerModel.getData().getAccount().getName());
                            Bundle bundle = new Bundle();
                            bundle.putString(IntentKeyGlobal.ACCOUNT_PASSWORD, password.get());
                            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_SCREENSHOT_WARNING).with(bundle).navigation();
                            finish();
                            dismissDialog();
                            ToastUtils.showShort(R.string.module_login_register_success);
                        } catch (Exception e) {
                            dismissDialog();
                            ToastUtils.showShort(R.string.net_work_failed);
                        }
                    }
                });
            }
        });


    }
}
