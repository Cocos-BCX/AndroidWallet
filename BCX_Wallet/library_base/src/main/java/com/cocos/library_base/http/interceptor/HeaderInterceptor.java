package com.cocos.library_base.http.interceptor;

import android.support.annotation.NonNull;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 请求头拦截器
 *
 * @author ningkang
 */
public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request build = chain.request().newBuilder()
                .build();
        return chain.proceed(build);
    }
}
