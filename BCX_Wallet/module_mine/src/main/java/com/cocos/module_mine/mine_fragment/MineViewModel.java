package com.cocos.module_mine.mine_fragment;

import android.app.Application;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.router.RouterActivityPath;

/**
 * @author ningkang.guo
 * @Date 2019/2/19
 */
public class MineViewModel extends BaseViewModel {

    public MineViewModel(@NonNull Application application) {
        super(application);
    }


    //资产总览按钮的点击事件
    public BindingCommand assetOverviewOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_ASSET_OVERVIEW).navigation();
        }
    });

    //钱包管理按钮的点击事件
    public BindingCommand walletManageOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_ACCOUNT_MANAGE_LIST).navigation();
        }
    });

    //多语言设置按钮的点击事件
    public BindingCommand systemSettingOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_SYSTEM_SETTING).navigation();
        }
    });

    //联系人按钮的点击事件
    public BindingCommand contactOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_CONTACT).navigation();
        }
    });

    //关于我们按钮的点击事件
    public BindingCommand aboutUsOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_ABOUT_US).navigation();
        }
    });

}
