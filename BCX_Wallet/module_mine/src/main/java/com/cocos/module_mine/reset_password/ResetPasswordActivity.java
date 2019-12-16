package com.cocos.module_mine.reset_password;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.module_mine.BR;
import com.cocos.module_mine.R;
import com.cocos.module_mine.databinding.ActivityResetPasswordBinding;

/**
 * 私钥导入登陆页面
 *
 * @author ningkang.guo
 * @Date 2019/1/28
 */

@Route(path = RouterActivityPath.ACTIVITY_RESET_PASSWORD)
public class ResetPasswordActivity extends BaseActivity<ActivityResetPasswordBinding, ResetPasswordViewModel> {

    private int from;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_reset_password;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        try {
            from = (int) getIntent().getExtras().get(IntentKeyGlobal.FROM);
        } catch (Exception e) {
        }
    }

    @Override
    public void initData() {
        viewModel.setFrom(from);
    }

    @Override
    public void onBackPressed() {
        if (from == 1) {
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_REGISTER).navigation();
        } else if (from == 2) {
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_PASSWORD_LOGIN).navigation();
        }
        finish();
    }
}