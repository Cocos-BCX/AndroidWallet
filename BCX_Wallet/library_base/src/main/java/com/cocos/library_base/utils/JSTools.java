package com.cocos.library_base.utils;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author YangJ
 * @date 2018/7/26
 */
public class JSTools {

    /**
     * 获取js文件内容
     *
     * @param context  参数为当前上下文对象
     * @param fileName 参数为要获取的js文件名称
     * @return
     */
    public static String getJS(Context context, String fileName) {
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            inputStream = context.getAssets().open(fileName);
            outputStream = new ByteArrayOutputStream();
            int len = 0;
            byte[] buffer = new byte[2048];
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            return new String(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
