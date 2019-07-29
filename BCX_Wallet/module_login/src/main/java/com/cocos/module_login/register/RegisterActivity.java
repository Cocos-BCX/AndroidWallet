package com.cocos.module_login.register;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.module_login.BR;
import com.cocos.module_login.R;
import com.cocos.module_login.databinding.ActivityRegisterBinding;

import java.util.Objects;

/**
 * 注册账号页面
 *
 * @author ningkang.guo
 * @Date 2019/1/31
 */

@Route(path = RouterActivityPath.ACTIVITY_REGISTER)
public class RegisterActivity extends BaseActivity<ActivityRegisterBinding, RegisterViewModel> {
    private int from;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_register;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        try {
            from = Objects.requireNonNull(getIntent().getExtras()).getInt(IntentKeyGlobal.FROM);
        } catch (Exception e) {
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (from == 3) {
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_PASSWORD_LOGIN).navigation();
        }
        finish();
    }
}
