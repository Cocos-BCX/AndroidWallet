package com.cocos.bcx_sdk.bcx_log;

import android.util.Log;

/**
 * LogUtils
 */

public class LogUtils {


    public static boolean isOpenLog;

    public static void v(String tag, String msg) {
        if (isOpenLog) {
            Log.v(tag, msg);

        }

    }

    public static void d(String tag, String msg) {
        if (isOpenLog) {
            Log.d(tag, msg);

        }

    }

    public static void i(String tag, String msg) {
        if (isOpenLog) {
            Log.i(tag, msg);

        }

    }

    public static void w(String tag, String msg) {
        if (isOpenLog) {
            Log.w(tag, msg);

        }

    }

    public static void e(String tag, String msg) {
        if (isOpenLog) {
            Log.e(tag, msg);

        }

    }
}
