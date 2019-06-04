package com.cocos.library_base.utils.singleton;


import com.google.gson.Gson;

/**
 * Gson单列对象类
 *
 * @author ningkang
 */

public class GsonSingleInstance {

    private static class GsonInstanceHolder {
        static final Gson INSTANCE = new Gson();
    }

    public static Gson getGsonInstance() {
        return GsonInstanceHolder.INSTANCE;
    }

}
