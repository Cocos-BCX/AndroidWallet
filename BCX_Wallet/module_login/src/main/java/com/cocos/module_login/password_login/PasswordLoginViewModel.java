package com.cocos.module_login.password_login;

import android.app.Application;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_login.R;
import com.cocos.module_login.entity.LoginModel;

/**
 * @author ningkang.guo
 * @Date 2019/3/29
 */
public class PasswordLoginViewModel extends BaseViewModel {

    public PasswordLoginViewModel(@NonNull Application application) {
        super(application);
    }

    public ObservableField<String> accountName = new ObservableField<>();
    public ObservableField<String> password = new ObservableField<>();

    //注册按钮的点击事件
    public BindingCommand registerClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_REGISTER).navigation();
        }
    });

    //导入按钮的点击事件
    public BindingCommand importOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle = new Bundle();
            bundle.putInt(IntentKeyGlobal.FROM, 2);
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_KEYLOGIN).with(bundle).navigation();
        }
    });

    //密码登录按钮的点击事件
    public BindingCommand passwordLoginClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (TextUtils.isEmpty(accountName.get())) {
                ToastUtils.showShort(R.string.module_login_account_info);
                return;
            }
            if (TextUtils.isEmpty(password.get())) {
                ToastUtils.showShort(R.string.module_login_password_empty);
                return;
            }
            showDialog();
            CocosBcxApiWrapper.getBcxInstance().password_login(accountName.get(), password.get(), new IBcxCallBack() {
                @Override
                public void onReceiveValue(final String s) {
                    MainHandler.getInstance().post(new Runnable() {
                        @Override
                        public void run() {
                            LoginModel loginModel = GsonSingleInstance.getGsonInstance().fromJson(s, LoginModel.class);
                            if (loginModel.code == 105) {
                                ToastUtils.showShort(R.string.module_login_password_error);
                                dismissDialog();
                                return;
                            }
                            if (!loginModel.isSuccess()) {
                                dismissDialog();
                                return;
                            }
                            AccountHelperUtils.setCurrentAccountName(loginModel.getData().getName());
                            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_MAIN_PATH).navigation();
                            finish();
                            dismissDialog();
                        }
                    });
                }
            });
        }
    });
}
