package com.cocos.library_base.utils;

import android.text.BidiFormatter;
import android.text.TextDirectionHeuristics;
import android.text.TextUtils;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author ningkang.guo
 * @Date 2019/7/16
 */
public class TimeUtil {

    /**
     * 用于解决时差8小时问题
     */
    public static String formDate(Date value) {
        String newValue = "null";
        try {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (value == null) {
                return "";
            }
            Calendar ca = Calendar.getInstance();
            ca.setTime(value);
            ca.add(Calendar.HOUR_OF_DAY, getTimeZone());
            newValue = f.format(ca.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newValue;
    }

    public static int getTimeZone() {
        Calendar mDummyDate;
        mDummyDate = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        mDummyDate.setTimeZone(now.getTimeZone());
        mDummyDate.set(now.get(Calendar.YEAR), 11, 31, 13, 0, 0);
        return now.getTimeZone().getRawOffset() / 1000 / 60 / 60;
    }

    public static String getTimeZoneText(TimeZone tz, boolean includeName) {
        Date now = new Date();

        SimpleDateFormat gmtFormatter = new SimpleDateFormat("ZZZZ");
        gmtFormatter.setTimeZone(tz);
        String gmtString = gmtFormatter.format(now);
        BidiFormatter bidiFormatter = BidiFormatter.getInstance();
        Locale l = Locale.getDefault();
        boolean isRtl = TextUtils.getLayoutDirectionFromLocale(l) == View.LAYOUT_DIRECTION_RTL;
        gmtString = bidiFormatter.unicodeWrap(gmtString,
                isRtl ? TextDirectionHeuristics.RTL : TextDirectionHeuristics.LTR);

        if (!includeName) {
            return gmtString;
        }

        return gmtString;
    }
}
