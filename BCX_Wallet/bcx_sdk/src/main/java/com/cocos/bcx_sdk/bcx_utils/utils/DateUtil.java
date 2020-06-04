package com.cocos.bcx_sdk.bcx_utils.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @author ningkang.guo
 * @Date 2019/11/11
 */
public class DateUtil {


    /**
     * 用于解决时差8小时问题
     */
    public static Date formDate(Date value) {
        try {
            Calendar ca = Calendar.getInstance();
            ca.setTime(value);
            ca.add(Calendar.HOUR_OF_DAY, getTimeZone());
            return ca.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getTimeZone() {
        Calendar mDummyDate;
        mDummyDate = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        mDummyDate.setTimeZone(now.getTimeZone());
        mDummyDate.set(now.get(Calendar.YEAR), 11, 31, 13, 0, 0);
        return now.getTimeZone().getRawOffset() / 1000 / 60 / 60;
    }

}
