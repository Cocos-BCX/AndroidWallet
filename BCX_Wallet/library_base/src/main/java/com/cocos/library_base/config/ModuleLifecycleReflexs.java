package com.cocos.library_base.config;

import android.support.annotation.Keep;

/**
 * 组件生命周期反射类名管理，在这里注册需要初始化的组件，通过反射动态调用各个组件的初始化方法
 * 注意：以下模块中初始化的Module类不能被混淆
 */
@Keep
public class ModuleLifecycleReflexs {
    private static final String BaseInit = "com.cocos.library_base.BaseModuleInit";

    private static final String AssetInit = "com.cocos.module_asset.AssetModuleInit";

    private static final String FoundInit = "com.cocos.module_found.FoundModuleInit";

    private static final String LoginInit = "com.cocos.module_login.LoginModuleInit";

    private static final String MineInit = "com.cocos.module_mine.MineModuleInit";

    private static final String ZxingInit = "com.renny.zxing.ZxingModuleInit";

    public static String[] initModuleNames = {BaseInit, AssetInit, FoundInit, LoginInit, MineInit, ZxingInit};
}