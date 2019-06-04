package com.cocos.module_login.backup;

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;

/**
 * @author ningkang.guo
 * @Date 2019/2/1
 */
public class ScreenshotWarningViewModel extends BaseViewModel {

    public String password;

    public ScreenshotWarningViewModel(@NonNull Application application) {
        super(application);
    }

    public BindingCommand confirmOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle = new Bundle();
            bundle.putString(IntentKeyGlobal.ACCOUNT_PASSWORD, password);
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_BACKUP_WALLET).with(bundle).navigation();
            finish();
        }
    });


    public void setAccountPassword(String password) {
        this.password = password;
    }
}
