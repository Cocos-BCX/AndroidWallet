package com.cocos.library_base.http.interceptor;

import android.support.annotation.NonNull;
import android.text.TextUtils;


import com.cocos.library_base.utils.AppNetworkMgr;
import com.cocos.library_base.utils.Utils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 云端响应头拦截器，用来配置缓存策略
 *
 * @author ningkang
 */
public class CacheControlInterceptor implements Interceptor {

    /**
     * 设缓存有效期为两天
     */
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;


    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        String cacheControl = request.cacheControl().toString();
        if (!AppNetworkMgr.isNetworkConnected(Utils.getContext())) {
            request = request.newBuilder()
                    .cacheControl(TextUtils.isEmpty(cacheControl) ? CacheControl
                            .FORCE_NETWORK : CacheControl.FORCE_CACHE)
                    .build();
        }
        Response originalResponse = chain.proceed(request);
        if (AppNetworkMgr.isNetworkConnected(Utils.getContext())) {
            return originalResponse.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
        } else {
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" +
                            CACHE_STALE_SEC)
                    .removeHeader("Pragma")
                    .build();
        }
    }
}
