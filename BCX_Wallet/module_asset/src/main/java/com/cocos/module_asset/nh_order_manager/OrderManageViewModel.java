package com.cocos.module_asset.nh_order_manager;

import android.app.Application;
import android.support.annotation.NonNull;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;

/**
 * @author ningkang.guo
 * @Date 2019/7/15
 */
public class OrderManageViewModel extends BaseViewModel {

    public OrderManageViewModel(@NonNull Application application) {
        super(application);
    }

    //条目的点击事件
    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });
}
