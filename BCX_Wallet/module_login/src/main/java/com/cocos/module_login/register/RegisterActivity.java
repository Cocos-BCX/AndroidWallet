package com.cocos.module_login.register;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.module_login.BR;
import com.cocos.module_login.R;
import com.cocos.module_login.databinding.ActivityRegisterBinding;

/**
 * 注册账号页面
 *
 * @author ningkang.guo
 * @Date 2019/1/31
 */

@Route(path = RouterActivityPath.ACTIVITY_REGISTER)
public class RegisterActivity extends BaseActivity<ActivityRegisterBinding, RegisterViewModel> {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_register;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

}
