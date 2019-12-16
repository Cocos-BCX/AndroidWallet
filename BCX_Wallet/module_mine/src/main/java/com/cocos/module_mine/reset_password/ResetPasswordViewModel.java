package com.cocos.module_mine.reset_password;

import android.annotation.SuppressLint;
import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.bcx_sdk.bcx_entity.AccountEntity;
import com.cocos.bcx_sdk.bcx_entity.AccountType;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.AccountEntities;
import com.cocos.library_base.entity.KeyLoginModel;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.RegexUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_mine.R;


/**
 * @author ningkang.guo
 * @Date 2019/1/28
 */
public class ResetPasswordViewModel extends BaseViewModel {

    private AccountEntity.AccountBean daoAccountModel;

    //私钥的绑定
    public ObservableField<String> privateKey = new ObservableField<>();

    public ObservableField<String> newPassword = new ObservableField<>();

    public ObservableField<String> confirmPassword = new ObservableField<>();


    public ResetPasswordViewModel(@NonNull Application application) {
        super(application);
    }

    //返回按钮的点击事件
    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });


    public void setDaoAccountModel(AccountEntity.AccountBean daoAccountModel) {
        this.daoAccountModel = daoAccountModel;
    }

    //重置按钮的点击事件
    public BindingCommand resetPwdOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            resetPwd();
        }
    });

    /**
     * 私钥登陆
     **/
    @SuppressLint("CheckResult")
    private void resetPwd() {
        if (TextUtils.isEmpty(privateKey.get())) {
            ToastUtils.showShort(R.string.module_login_private_key_empty);
            return;
        }
        if (TextUtils.isEmpty(newPassword.get())) {
            ToastUtils.showShort(R.string.module_mine_modify_password_empty);
            return;
        }

        if (TextUtils.isEmpty(confirmPassword.get())) {
            ToastUtils.showShort(R.string.module_mine_confirm_password_empty);
            return;
        }

        if (!TextUtils.equals(confirmPassword.get(), newPassword.get())) {
            ToastUtils.showShort(R.string.module_mine_modify_password_confirm_failure);
            return;
        }

        if (!RegexUtils.isLegalKey(privateKey.get())) {
            ToastUtils.showShort(R.string.module_login_key_format_error);
            return;
        }
        showDialog();
        CocosBcxApiWrapper.getBcxInstance().get_account_name_by_private_key(privateKey.get(), new IBcxCallBack() {
            @SuppressLint("LongLogTag")
            @Override
            public void onReceiveValue(String s) {
                MainHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("get_account_name_by_private_key", s);
                        AccountEntities accountEntities = GsonSingleInstance.getGsonInstance().fromJson(s, AccountEntities.class);
                        if (null == accountEntities.data || accountEntities.data.size() <= 0) {
                            dismissDialog();
                            ToastUtils.showShort(R.string.module_login_key_format_error);
                            return;
                        }
                        for (com.cocos.library_base.entity.AccountEntity accountEntity : accountEntities.data) {
                            if (TextUtils.equals(accountEntity.name, daoAccountModel.getName()) && TextUtils.equals(accountEntity.id, daoAccountModel.getId())) {
                                CocosBcxApiWrapper.getBcxInstance().import_wif_key(privateKey.get(), confirmPassword.get(), AccountType.WALLET.name(),
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
                                                        if (!keyLoginModel.isSuccess()) {
                                                            ToastUtils.showShort(R.string.net_work_failed);
                                                            dismissDialog();
                                                            return;
                                                        }
                                                        AccountHelperUtils.setCurrentAccountName(keyLoginModel.data.get(0));
                                                        ARouter.getInstance().build(RouterActivityPath.ACTIVITY_MAIN_PATH).navigation();
                                                        ToastUtils.showShort(R.string.module_mine_reset_password_success);
                                                        finish();
                                                        dismissDialog();
                                                    }
                                                });
                                            }
                                        });
                                break;
                            }
                            ToastUtils.showShort(R.string.module_login_key_format_error);
                            dismissDialog();
                        }
                    }
                });
            }
        });

    }

}
