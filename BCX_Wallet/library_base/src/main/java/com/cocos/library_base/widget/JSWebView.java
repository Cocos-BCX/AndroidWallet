package com.cocos.library_base.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.cocos.library_base.R;
import com.cocos.library_base.base.BaseWebView;
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
    }

}