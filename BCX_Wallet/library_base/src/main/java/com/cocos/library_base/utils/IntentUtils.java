package com.cocos.library_base.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cocos.library_base.invokedpages.model.BaseInvokeModel;
import com.cocos.library_base.invokedpages.model.BaseInvokeResultModel;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;

/**
 * @author ningkang.guo
 * @Date 2018/12/10
 */
public class IntentUtils {

    public static void startActivitySafely(Context context, Class cls) {
        Intent intent = new Intent(context, cls);
        if (context == null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            Utils.getContext().startActivity(intent);
        } else {
            context.startActivity(intent);
        }
    }

    public static void startActivityWithBundle(Context context, Class cls, Bundle bundle) {
        Intent intent = new Intent(context, cls);
        if (context == null) {
            if (null != bundle) {
                intent.putExtras(bundle);
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                Utils.getContext().startActivity(intent);
            }
        } else {
            if (null != bundle) {
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        }
    }


    public static void jumpToDapp(Context context, BaseInvokeResultModel baseInvokeResultModel, BaseInvokeModel baseInfo) {
        ComponentName component = new ComponentName(baseInfo.getPackageName(), baseInfo.getClassName());
        Intent intent = new Intent();
        intent.setComponent(component);
        intent.putExtra("result", GsonSingleInstance.getGsonInstance().toJson(baseInvokeResultModel));
        context.startActivity(intent);
    }

    public static void jumpToDappWithCancel(Context context, BaseInvokeModel baseInfo, String actionId) {
        try {
            ComponentName component = new ComponentName(baseInfo.getPackageName(), baseInfo.getClassName());
            BaseInvokeResultModel baseInvokeResultModel = new BaseInvokeResultModel();
            baseInvokeResultModel.setCode(0);
            baseInvokeResultModel.setMessage("canceled");
            baseInvokeResultModel.setActionId(actionId);
            Intent intent = new Intent();
            intent.setComponent(component);
            intent.putExtra("result", GsonSingleInstance.getGsonInstance().toJson(baseInvokeResultModel));
            context.startActivity(intent);
        } catch (Exception e) {

        }
    }

    public static void jumpToDappWithError(Context context, BaseInvokeModel baseInfo, String actionId, String message) {
        try {
            ComponentName component = new ComponentName(baseInfo.getPackageName(), baseInfo.getClassName());
            BaseInvokeResultModel baseInvokeResultModel = new BaseInvokeResultModel();
            baseInvokeResultModel.setCode(-1);
            baseInvokeResultModel.setMessage(message);
            baseInvokeResultModel.setActionId(actionId);
            Intent intent = new Intent();
            intent.setComponent(component);
            intent.putExtra("result", GsonSingleInstance.getGsonInstance().toJson(baseInvokeResultModel));
            context.startActivity(intent);
        } catch (Exception e) {

        }
    }
}
