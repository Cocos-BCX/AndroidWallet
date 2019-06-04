package com.cocos.module_mine.about_us;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.module_mine.BR;
import com.cocos.module_mine.R;
import com.cocos.module_mine.databinding.ActivityAboutUsBinding;

/**
 * @author ningkang.guo
 * @Date 2019/2/22
 */

@Route(path = RouterActivityPath.ACTIVITY_ABOUT_US)
public class AboutUsActivity extends BaseActivity<ActivityAboutUsBinding, AboutUsViewModel> {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_about_us;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}
