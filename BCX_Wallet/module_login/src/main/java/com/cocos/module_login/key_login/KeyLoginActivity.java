package com.cocos.module_login.key_login;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.module_login.BR;
import com.cocos.module_login.R;
import com.cocos.module_login.databinding.ActivityKeyLoginBinding;

/**
 * 私钥导入登陆页面
 *
 * @author ningkang.guo
 * @Date 2019/1/28
 */

@Route(path = RouterActivityPath.ACTIVITY_KEYLOGIN)
public class KeyLoginActivity extends BaseActivity<ActivityKeyLoginBinding, KeyLoginViewModel> {

    private int from;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_key_login;
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
        binding.etTempPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        pwdVisibleControl(binding.etTempPwd, binding.ivPwdVisible);
        binding.ivPwdVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwdVisibleControl(binding.etTempPwd, binding.ivPwdVisible);
            }
        });
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
