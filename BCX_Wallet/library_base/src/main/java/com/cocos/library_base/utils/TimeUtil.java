package com.cocos.library_base.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
            ca.add(Calendar.HOUR_OF_DAY, 8);
            newValue = f.format(ca.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newValue;
    }
}
