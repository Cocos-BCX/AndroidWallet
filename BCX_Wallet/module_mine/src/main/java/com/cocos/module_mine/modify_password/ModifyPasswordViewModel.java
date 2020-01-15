package com.cocos.module_mine.modify_password;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.BaseResult;
import com.cocos.library_base.utils.KLog;
import com.cocos.library_base.utils.RegexUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_mine.R;

/**
 * @author ningkang.guo
 * @Date 2019/2/20
 */
public class ModifyPasswordViewModel extends BaseViewModel {


    public ModifyPasswordViewModel(@NonNull Application application) {
        super(application);
    }

    String accountNameStr;

    //旧密码的绑定
    public ObservableField<String> currentPassword = new ObservableField<>();

    //新密码的绑定
    public ObservableField<String> newPassword = new ObservableField<>();

    //确认密码的绑定
    public ObservableField<String> confirmPassword = new ObservableField<>();

    //返回按钮的点击事件
    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    //修改按钮的点击事件
    public BindingCommand modifyPasswordOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            modifyPassword();
        }
    });

    private void modifyPassword() {
        if (TextUtils.isEmpty(currentPassword.get()) || TextUtils.isEmpty(confirmPassword.get()) || TextUtils.isEmpty(newPassword.get())) {
            ToastUtils.showShort(R.string.module_mine_modify_password_empty);
            return;
        }

        if (!TextUtils.equals(newPassword.get(), confirmPassword.get())) {
            ToastUtils.showShort(R.string.module_mine_modify_password_confirm_failure);
            return;
        }

        if (!RegexUtils.isLegalPassword(newPassword.get())) {
            ToastUtils.showShort(R.string.module_mine_modify_password_illegal);
            return;
        }

        showDialog();
        CocosBcxApiWrapper.getBcxInstance().modify_password(accountNameStr, currentPassword.get(), confirmPassword.get(), new IBcxCallBack() {
            @Override
            public void onReceiveValue(final String s) {
                MainHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        KLog.i("changePassword", s);
                        BaseResult baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResult.class);
                        if (baseResult.getCode() == 105) {
                            dismissDialog();
                            ToastUtils.showShort(R.string.module_mine_wrong_password);
                            return;
                        }
                        if (baseResult.getCode() == 104) {
                            dismissDialog();
                            ToastUtils.showShort(R.string.account_not_found);
                            return;
                        }
                        if (baseResult.getCode() == 109) {
                            dismissDialog();
                            ToastUtils.showShort(R.string.module_login_key_format_error);
                            return;
                        }
                        if (baseResult.code == 112) {
                            dismissDialog();
                            ToastUtils.showShort(R.string.module_asset_private_key_author_failed_owner);
                            return;
                        }

                        if (baseResult.getMessage().contains("Insufficient Balance")) {
                            dismissDialog();
                            ToastUtils.showShort(R.string.module_asset_operate_fee_not_much);
                            return;
                        }

                        if (!baseResult.isSuccess()) {
                            dismissDialog();
                            return;
                        }
                        ToastUtils.showShort(R.string.module_mine_modify_passcode_success);
                        dismissDialog();
                        finish();
                    }
                });
            }
        });

    }

    public void setAccountName(String accountName) {
        this.accountNameStr = accountName;
    }
}
