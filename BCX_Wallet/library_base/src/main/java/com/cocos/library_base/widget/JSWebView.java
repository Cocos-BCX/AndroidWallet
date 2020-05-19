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

import com.cocos.library_base.R;
import com.cocos.library_base.base.BaseWebView;
import com.cocos.library_base.base.JavaScriptUtil;
import com.cocos.library_base.utils.Utils;

public class JSWebView extends BaseWebView {

    private ProgressView progressView;

    private Context context;

    public JSWebView(Context context) {
        this(context, null);
    }

    public JSWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JSWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initialize();
    }

    @SuppressLint({"SetJavaScriptEnabled", "ObsoleteSdkInt"})
    private void initialize() {
        progressView = new ProgressView(context);
        progressView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(2)));
        progressView.setColor(getResources().getColor(R.color.color_262A33));
        progressView.setProgress(10);
        addView(progressView);
        WebSettings settings = getSettings();
        initializeSettings(settings);
        setWebChromeClient(new JsWebChromeClient());
    }

    @SuppressWarnings("deprecation")
    @SuppressLint({"SetJavaScriptEnabled", "NewApi", "ObsoleteSdkInt"})
    public void initializeSettings(WebSettings settings) {
        settings.setJavaScriptEnabled(true);
        addJavascriptInterface(new JavaScriptUtil(), "DappJsBridge");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            settings.setAppCacheMaxSize(Long.MAX_VALUE);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.setEnableSmoothTransition(true);
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            settings.setMediaPlaybackRequiresUserGesture(true);
        }

        WebView.setWebContentsDebuggingEnabled(true);
        getSettings().setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setLoadsImagesAutomatically(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setDatabaseEnabled(true);
        settings.setDisplayZoomControls(false);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(false);
        settings.setRenderPriority(WebSettings.RenderPriority.LOW);
        setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setAllowFileAccessFromFileURLs(false);
        settings.setAllowUniversalAccessFromFileURLs(false);
        // 特别注意：5.1以上默认禁止了https和http混用，以下方式是开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        }
    }

    private class JsWebChromeClient extends WebChromeClient {
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

}