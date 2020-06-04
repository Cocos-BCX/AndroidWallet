package com.cocos.bcx_sdk.bcx_utils.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ningkang.guo
 * @Date 2019/10/16
 */
public class IDHelper {

    public static List<String> getIds(int headNumber, int middleNumber, int startNumber, int count) {
        List<String> ids = new ArrayList<>();
        for (int i = startNumber; i < count; i++) {
            StringBuilder stringBuffer = new StringBuilder(headNumber + "." + middleNumber + ".");
            stringBuffer.append(i);
            ids.add(stringBuffer.toString());
            stringBuffer.setLength(0);
        }
        return ids;
    }
}
