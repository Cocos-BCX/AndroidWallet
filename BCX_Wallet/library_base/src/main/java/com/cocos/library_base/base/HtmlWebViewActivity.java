package com.cocos.library_base.base;

import android.databinding.Observable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.bcx_sdk.bcx_log.LogUtils;
import com.cocos.library_base.R;
import com.cocos.library_base.databinding.ActivityHtmlWebviewBindingImpl;
import com.cocos.library_base.entity.WebViewModel;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.multi_language.LocalManageUtil;
import com.cocos.library_base.utils.multi_language.SPUtil;

import me.tatarka.bindingcollectionadapter2.BR;

/**
 * @author ningkang.guo
 * @Date 2019/3/5
 */

@Route(path = RouterActivityPath.ACTIVITY_HTML_WEB)
public class HtmlWebViewActivity extends BaseActivity<ActivityHtmlWebviewBindingImpl, HtmlWebViewViewModel> {

    private WebViewModel webViewModel;

    @Override
    public int initContentView(Bundle bundle) {
        return R.layout.activity_html_webview;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLangeuage();
    }

    private void initLangeuage() {
        LocalManageUtil.setApplicationLanguage(this);
    }

    @Override
    public void initParam() {
        try {
            Bundle bundle = getIntent().getExtras();
            webViewModel = (WebViewModel) bundle.getSerializable(IntentKeyGlobal.WEB_MODEL);
        } catch (Exception e) {
        }
    }

    @Override
    public void initData() {

        viewModel.setWebData(webViewModel);


        if (!TextUtils.isEmpty(webViewModel.getUrl())) {
            binding.htmlWebView.loadUrl(webViewModel.getUrl());
        }
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.backObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (binding.htmlWebView.canGoBack()) {
                    binding.htmlWebView.goBack();
                    return;
                }
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (binding.htmlWebView.canGoBack()) {
                binding.htmlWebView.goBack();
                return true;
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        CookieSyncManager.createInstance(this);  //Create a singleton CookieSyncManager within a context
        CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
        cookieManager.removeAllCookie();// Removes all cookies.
        CookieSyncManager.getInstance().sync(); // forces sync manager to sync now
        binding.htmlWebView.setWebChromeClient(null);
        binding.htmlWebView.setWebViewClient(null);
        binding.htmlWebView.getSettings().setJavaScriptEnabled(false);
        binding.htmlWebView.destroy();
        super.onDestroy();
    }

}
