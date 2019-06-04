package com.cocos.library_base.http.callback;


import android.app.Activity;

import com.cocos.library_base.http.Platform;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * 数据返回结果回调
 *
 * @author ningkang
 */

public abstract class BaseObserver<T> implements Observer<T> {

    private Platform mPlatform;


    public BaseObserver() {
        mPlatform = Platform.get();
    }

    public BaseObserver(Activity mActivity) {
        mPlatform = Platform.get();
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (null != d) {
            onBaseDisposable(d);
        }
    }

    @Override
    public void onNext(final T value) {
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                onBaseNext(value);
            }
        });
    }

    @Override
    public void onError(final Throwable e) {
        mPlatform.execute(() -> onBaseError(e));
    }

    @Override
    public void onComplete() {
        onBaseComplete();
    }

    protected void onBaseError(Throwable t) {
    }


    protected abstract void onBaseNext(T data);

    protected void onBaseComplete() {
    }

    private void onBaseDisposable(Disposable d) {
    }

}