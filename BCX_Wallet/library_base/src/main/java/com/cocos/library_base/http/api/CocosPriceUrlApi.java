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

public class CocosPriceUrlApi {


    private static ApiServer apiServer;

    public static ApiServer getApiBaseService() {
        if (apiServer == null) {
            synchronized (CocosPriceUrlApi.class) {
                if (apiServer == null) {
                    new CocosPriceUrlApi();
                }
            }
        }
        return apiServer;
    }


    private CocosPriceUrlApi() {
        Retrofit baseUrlRetrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.COCOS_PRICE_BASE)
                .client(BaseApi.getClient())
                .addConverterFactory(CustomGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        apiServer = baseUrlRetrofit.create(ApiServer.class);
    }


}
