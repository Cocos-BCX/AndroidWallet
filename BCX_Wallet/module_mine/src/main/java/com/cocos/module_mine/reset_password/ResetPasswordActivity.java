package com.cocos.module_mine.reset_password;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.bcx_sdk.bcx_entity.AccountEntity;
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

    private AccountEntity.AccountBean daoAccountModel;

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
            daoAccountModel = (AccountEntity.AccountBean) getIntent().getSerializableExtra(IntentKeyGlobal.DAO_ACCOUNT_MODEL);
        } catch (Exception e) {
        }
    }

    @Override
    public void initData() {
        viewModel.setDaoAccountModel(daoAccountModel);
        binding.etResetPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        binding.etResetConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        pwdVisibleControl(binding.etResetPassword, binding.ivPwdVisible);
        pwdVisibleControl(binding.etResetConfirmPassword, binding.ivPwdVisible);
        binding.ivPwdVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwdVisibleControl(binding.etResetPassword, binding.ivPwdVisible);
                pwdVisibleControl(binding.etResetConfirmPassword, binding.ivPwdVisible);
            }
        });
    }
}
