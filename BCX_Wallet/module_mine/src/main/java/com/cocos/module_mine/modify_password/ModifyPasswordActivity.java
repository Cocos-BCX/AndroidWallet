package com.cocos.module_mine.modify_password;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.bcx_sdk.bcx_entity.AccountEntity;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.module_mine.BR;
import com.cocos.module_mine.R;
import com.cocos.module_mine.databinding.ActivityModifyPasswordBinding;

/**
 * @author ningkang.guo
 * @Date 2019/2/20
 */
@Route(path = RouterActivityPath.ACTIVITY_MODIFY_PASSWORD)
public class ModifyPasswordActivity extends BaseActivity<ActivityModifyPasswordBinding, ModifyPasswordViewModel> {


    private AccountEntity.AccountBean daoAccountModel;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_modify_password;
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
        viewModel.setAccountName(daoAccountModel.getName());
        binding.etModifyPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        binding.etModifyConfirm.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        binding.etSetPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        pwdVisibleControl( binding.etSetPassword, binding.ivOldPwdVisible);
        pwdVisibleControl2(binding.etModifyPassword, binding.etModifyConfirm, binding.ivPwdVisible);
        binding.ivPwdVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwdVisibleControl2(binding.etModifyPassword, binding.etModifyConfirm,binding.ivPwdVisible);
            }
        });
        binding.ivOldPwdVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwdVisibleControl(binding.etSetPassword, binding.ivOldPwdVisible);
            }
        });
    }

    protected boolean isOldVisible = true;

    /**
     * 密码输入框右边的眼睛点击改变状态逻辑
     */
    public void pwdVisibleControl(EditText editText, ImageView imageView) {
        if (isOldVisible) {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
            imageView.setImageResource(com.cocos.library_base.R.drawable.hidden_pwd);
        } else {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_CLASS_TEXT);
            imageView.setImageResource(com.cocos.library_base.R.drawable.show_pwd);
        }
        isOldVisible = !isOldVisible;
        String pwd = editText.getText().toString().trim();
        editText.setSelection(pwd.length());
    }

}
