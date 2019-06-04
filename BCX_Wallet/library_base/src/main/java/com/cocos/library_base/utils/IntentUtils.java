package com.cocos.library_base.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
}
