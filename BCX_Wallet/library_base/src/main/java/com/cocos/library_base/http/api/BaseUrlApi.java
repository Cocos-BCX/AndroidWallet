package com.cocos.library_base.http.api;

import com.cocos.library_base.BuildConfig;
import com.cocos.library_base.http.apiserver.ApiServer;
import com.cocos.library_base.http.convert.CustomGsonConverterFactory;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * retrofit 设置Base64转换器
 *
 * @author ningkang
 */

public class BaseUrlApi {


    private static ApiServer apiServer;

    public static ApiServer getApiBaseService() {
        if (apiServer == null) {
            synchronized (BaseUrlApi.class) {
                if (apiServer == null) {
                    new BaseUrlApi();
                }
            }
        }
        return apiServer;
    }


    private BaseUrlApi() {
        Retrofit baseUrlRetrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_BASE)
                .client(BaseApi.getClient())
                .addConverterFactory(CustomGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        apiServer = baseUrlRetrofit.create(ApiServer.class);
    }


}
