package com.cocos.library_base.http.api;

import com.cocos.library_base.http.cookie.AddCookiesInterceptor;
import com.cocos.library_base.http.cookie.SaveCookiesInterceptor;
import com.cocos.library_base.http.interceptor.CacheControlInterceptor;
import com.cocos.library_base.http.interceptor.HeaderInterceptor;
import com.cocos.library_base.utils.Utils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * 创建okHttpClient，配置请求参数
 *
 * @author ningkang
 */

public class BaseApi {

    private static final int READ_TIME_OUT = 10;
    private static final int CONNECT_TIME_OUT = 10;

    private static OkHttpClient client;


    public static OkHttpClient getClient() {
        if (client == null) {
            synchronized (BaseApi.class) {
                if (client == null) {
                    new BaseApi();
                }
            }
        }
        return client;
    }

    private BaseApi() {
        CacheControlInterceptor controlInterceptor = new CacheControlInterceptor();
        HeaderInterceptor headerInterceptor = new HeaderInterceptor();
        File cacheFile = new File(Utils.getContext().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(controlInterceptor)
                .addNetworkInterceptor(controlInterceptor)
                .addInterceptor(headerInterceptor)
                .addInterceptor(new SaveCookiesInterceptor())
                .addInterceptor(new AddCookiesInterceptor())
                .cache(cache);
        client = builder.build();
    }


}
