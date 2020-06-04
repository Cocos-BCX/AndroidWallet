package com.cocos.bcx_sdk.bcx_utils.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hoolai
 * @date 2017/6/16
 * 密码校验工具类
 */

public class PassWordCheckUtil {


    /**
     * verify password
     */
    public static boolean passwordVerify(String str) {
        return verify("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*.-]).{12,}$", str) && verify("^[^\\s]{12,}$",str)&& !isContainChinese(str);
    }

    /**
     * verify regex
     */
    public static boolean verify(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    /**
     * is contains chainese
     */
    public static boolean isContainChinese(String str) {

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
}