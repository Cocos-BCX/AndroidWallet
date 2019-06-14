package com.cocos.library_base;

import android.app.Application;

import com.cocos.library_base.config.IModuleInit;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.multi_language.LocalManageUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author ningkang.guo
 * @Date 2019/1/28
 */
public class BaseModuleInit implements IModuleInit {
    @Override
    public boolean onInitAhead(Application application) {
        Utils.init(application);
        // 友盟统计
        UMConfigure.init(application, UMConfigure.DEVICE_TYPE_PHONE, "");
        MobclickAgent.setCatchUncaughtExceptions(true);
        UMConfigure.setLogEnabled(BuildConfig.IS_TEST_ENV);
        UMConfigure.setEncryptEnabled(true);
        //  需要在Sp初始化完成后调用
        LocalManageUtil.setApplicationLanguage(application);
        closeAndroidPDialog();
        return true;
    }

    @Override
    public boolean onInitLow(Application application) {
        return false;
    }

    private void closeAndroidPDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
