package com.cocos.library_base.http.http;


import android.accounts.NetworkErrorException;


import com.cocos.library_base.R;
import com.cocos.library_base.http.callback.BaseObserver;
import com.cocos.library_base.utils.AppNetworkMgr;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * HttpMethods
 *
 * @author ningkang
 */
public class HttpMethods {

    /**
     * 订阅请求
     */
    public static <T> void toSubscribe(Observable<T> observable, BaseObserver<T> observer) {

        if (!AppNetworkMgr.isNetworkConnected(Utils.getContext())) {
            ToastUtils.showShortCenter(R.string.network_error);
            return;
        }
        // 指定subscribe()发生在IO线程
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                    private int mRetryCount;

                    @Override
                    public ObservableSource<?> apply(@NonNull Observable<Throwable> throwableObservable) {
                        return throwableObservable.flatMap((Function<Throwable, ObservableSource<?>>) throwable -> {
                            boolean exceptionType = (throwable instanceof NetworkErrorException
                                    || throwable instanceof ConnectException
                                    || throwable instanceof SocketTimeoutException
                                    || throwable instanceof TimeoutException) && mRetryCount < 3;
                            if (exceptionType) {
                                mRetryCount++;
                                return Observable.timer(4000, TimeUnit.MILLISECONDS);
                            }
                            return Observable.error(throwable);
                        });
                    }
                })
                .subscribe(observer);
    }

}