package com.cocos.module_mine.modify_password;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.BaseResult;
import com.cocos.library_base.utils.RegexUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.module_mine.R;

/**
 * @author ningkang.guo
 * @Date 2019/2/20
 */
public class ModifyPasswordViewModel extends BaseViewModel {

    private BaseResult baseResult;

    public ModifyPasswordViewModel(@NonNull Application application) {
        super(application);
    }

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

        if (!RegexUtils.isLegalPassword(newPassword.get())) {
            ToastUtils.showShort(R.string.module_mine_modify_password_illegal);
            return;
        }

        if (!TextUtils.equals(newPassword.get(), confirmPassword.get())) {
            ToastUtils.showShort(R.string.module_mine_modify_password_confirm_failure);
            return;
        }

        showDialog();
    /*    BcxSDkInstance.getBcxInstance().changePassword(currentPassword.get(), confirmPassword.get(), new IBcxCallBack() {
            @Override
            public void onReceiveValue(final String s) {
                MainHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        KLog.i("changePassword", s);
                        baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResult.class);
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
*/
    }
}
