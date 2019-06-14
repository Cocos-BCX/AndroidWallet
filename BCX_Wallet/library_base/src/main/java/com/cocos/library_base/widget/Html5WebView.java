package com.cocos.library_base.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cocos.library_base.R;
import com.cocos.library_base.base.BaseWebView;
import com.cocos.library_base.utils.Utils;

public class Html5WebView extends BaseWebView {
    private ProgressView progressView;
    private Context context;

    public Html5WebView(Context context) {
        this(context, null);
    }

    public Html5WebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Html5WebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        progressView = new ProgressView(context);
        progressView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(2)));
        progressView.setColor(getResources().getColor(R.color.color_262A33));
        progressView.setProgress(10);
        addView(progressView);
        initWebSettings();
        setWebChromeClient(new MyWebChromeClient());
        setWebViewClient(new MyWebviewClient());
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebSettings() {
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        getSettings().setUseWideViewPort(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }


    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressView.setVisibility(View.GONE);
            } else {
                progressView.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    private class MyWebviewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    }

}
