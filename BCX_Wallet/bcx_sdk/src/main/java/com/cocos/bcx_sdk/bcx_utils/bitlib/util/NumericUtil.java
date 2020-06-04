package com.cocos.bcx_sdk.bcx_utils.bitlib.util;

import android.text.TextUtils;
import javax.annotation.Nullable;

public final class NumericUtil {

    private NumericUtil() {}

    public static long parseLong(@Nullable String value) {
        return parseLong(value, 0L);
    }

    public static long parseLong(@Nullable String value, long defaultVal) {
        if (TextUtils.isEmpty(value)) {
            return defaultVal;
        }

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return defaultVal;
    }

    public static double parseDouble(@Nullable String value) {
        return parseDouble(value, 0.0D);
    }

    public static double parseDouble(@Nullable String value, double defaultVal) {
        if (TextUtils.isEmpty(value)) {
            return defaultVal;
        }

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return defaultVal;
    }
}
