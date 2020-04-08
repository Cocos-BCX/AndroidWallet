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
        pwdVisibleControl(binding.etModifyPassword, binding.etModifyConfirm, binding.etSetPassword, binding.ivPwdVisible);
        binding.ivPwdVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwdVisibleControl2(binding.etModifyConfirm, binding.etModifyPassword, binding.ivPwdVisible);
            }
        });
    }

    /**
     * 密码输入框右边的眼睛点击改变状态逻辑
     */
    public void pwdVisibleControl(EditText editText1, EditText editText2, EditText editText3, ImageView imageView) {
        if (isPwdCancel) {
            editText1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
            editText2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
            editText3.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
            imageView.setImageResource(com.cocos.library_base.R.drawable.hidden_pwd);
        } else {
            editText1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_CLASS_TEXT);
            editText2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_CLASS_TEXT);
            editText3.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_CLASS_TEXT);
            imageView.setImageResource(com.cocos.library_base.R.drawable.show_pwd);
        }
        isPwdCancel = !isPwdCancel;
        String pwd = editText1.getText().toString().trim();
        String newpwd = editText2.getText().toString().trim();
        String oldPwd = editText3.getText().toString().trim();
        editText1.setSelection(pwd.length());
        editText2.setSelection(newpwd.length());
        editText3.setSelection(oldPwd.length());
    }
}
