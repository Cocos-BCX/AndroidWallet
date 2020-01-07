package com.cocos.library_base.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cocos.library_base.R;
import com.cocos.library_base.utils.Utils;

public class BaseWebView extends WebView {

    private boolean mIsError;
    private View mErrorView;
    private Context context;

    // TAG
    private static final String TAG = "BaseWebView";

    public BaseWebView(Context context) {
        super(context);
        initialize(context);
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        this.context = context;
    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        super.setWebViewClient(new BaseWebViewClient(client));
    }

    @Override
    public void setWebChromeClient(WebChromeClient client) {
        super.setWebChromeClient(new BaseWebChromeClient(client));
    }

    private View createErrorView() {
        if (mErrorView == null) {
            LayoutInflater inflater = LayoutInflater.from(Utils.getContext());
            mErrorView = inflater.inflate(R.layout.include_web_error, null);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mErrorView.setLayoutParams(params);
            mErrorView.findViewById(R.id.tv_error_reload).setOnClickListener(view -> reload());
            mErrorView.setTag(TAG);
        }
        return mErrorView;
    }

    private void showError() {
        ViewGroup group = (ViewGroup) getParent();
        if (null == group) {
            return;
        }
        View view = group.findViewWithTag(TAG);
        if (view == null) {
            group.addView(createErrorView());
        } else {

        }
    }

    private void hideWebView() {
        // 隐藏WebView
        if (getVisibility() == View.GONE) {
            return;
        }
        setVisibility(View.GONE);
    }

    private void hideError() {
        ViewGroup group = (ViewGroup) getParent();
        if (null == group) {
            return;
        }
        View view = group.findViewWithTag(TAG);
        if (view == null) {

        } else {
            group.removeView(view);
        }
    }

    private void showWebView() {
        // 显示WebView
        if (getVisibility() == View.VISIBLE) {
            return;
        }
        setVisibility(View.VISIBLE);
    }

    private class BaseWebViewClient extends WebViewClient {

        private WebViewClient mClient;

        public BaseWebViewClient(WebViewClient client) {
            mClient = client;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mIsError = false;
            hideError();
            if (mClient == null) {
                return;
            }
            mClient.onPageStarted(view, url, favicon);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            if (mClient == null) {
                return;
            }
            if (url.endsWith(".apk")) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            } else if (url.contains(".apk?")) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
            mClient.onLoadResource(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            if (mClient == null) {
                return;
            }
            mClient.onReceivedSslError(view, handler, error);
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            if (mClient == null) {
                return;
            }
            mClient.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mIsError = true;
            if (mClient == null) {
                return;
            }
            mClient.onReceivedError(view, errorCode, description, failingUrl);
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (request.isForMainFrame()) {
                mIsError = true;
            }
            if (mClient == null) {
                return;
            }
            mClient.onReceivedError(view, request, error);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mIsError) {
                showError();
                hideWebView();
            } else {
                hideError();
                showWebView();
            }
            if (mClient == null) {
                return;
            }
            mClient.onPageFinished(view, url);
        }
    }

    private class BaseWebChromeClient extends WebChromeClient {
        private WebChromeClient mClient;

        public BaseWebChromeClient(WebChromeClient client) {
            mClient = client;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (mClient == null) {
                return;
            }
            mClient.onReceivedTitle(view, title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (mClient == null) {
                return;
            }
            mClient.onProgressChanged(view, newProgress);
        }
    }


}