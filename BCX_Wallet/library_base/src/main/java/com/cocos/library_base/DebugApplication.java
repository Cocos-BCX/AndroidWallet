package com.cocos.library_base;

import android.app.Application;

import com.cocos.library_base.config.ModuleLifecycleConfig;

/**
 * 模块独立运行时用于初始化的application
 *
 * @author ningkang.guo
 * @Date 2019/1/28
 */
public class DebugApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ModuleLifecycleConfig.getInstance().initModuleAhead(this); //初始化组件(靠前)
        ModuleLifecycleConfig.getInstance().initModuleLow(this);     //初始化组件(靠后)
    }
}
