package com.cocos.module_login.backup;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.module_login.BR;
import com.cocos.module_login.R;
import com.cocos.module_login.databinding.ActivityScreenshotWarningBinding;

/**
 * @author ningkang.guo
 * @Date 2019/2/1
 */

@Route(path = RouterActivityPath.ACTIVITY_SCREENSHOT_WARNING)
public class ScreenshotWarningActivity extends BaseActivity<ActivityScreenshotWarningBinding, ScreenshotWarningViewModel> {

    private String password;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_screenshot_warning;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        try {
            password = getIntent().getExtras().getString(IntentKeyGlobal.ACCOUNT_PASSWORD);
        } catch (Exception e) {
        }
    }

    @Override
    public void initData() {
        viewModel.setAccountPassword(password);
    }
}
