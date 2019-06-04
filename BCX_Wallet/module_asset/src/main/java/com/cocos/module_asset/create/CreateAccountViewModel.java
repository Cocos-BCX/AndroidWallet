package com.cocos.module_asset.create;

import android.app.Application;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.router.RouterActivityPath;

/**
 * @author ningkang.guo
 * @Date 2019/1/31
 */
public class CreateAccountViewModel extends BaseViewModel {

    public CreateAccountViewModel(@NonNull Application application) {
        super(application);
    }

    //备份钱包返回上层
    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    //导入钱包按钮的点击事件
    public BindingCommand importWalletOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_KEYLOGIN).navigation();
        }
    });


    //创建钱包按钮的点击事件
    public BindingCommand createWalletOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_REGISTER).navigation();
        }
    });


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
