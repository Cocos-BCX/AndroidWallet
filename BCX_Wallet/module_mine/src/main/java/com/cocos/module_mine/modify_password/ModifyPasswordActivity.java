package com.cocos.module_mine.modify_password;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.StatusBarUtils;
import com.cocos.module_mine.BR;
import com.cocos.module_mine.R;
import com.cocos.module_mine.account_manage.AccountManageActivity;
import com.cocos.module_mine.databinding.ActivityModifyPasswordBinding;

/**
 * @author ningkang.guo
 * @Date 2019/2/20
 */
@Route(path = RouterActivityPath.ACTIVITY_MODIFY_PASSWORD)
public class ModifyPasswordActivity extends BaseActivity<ActivityModifyPasswordBinding, ModifyPasswordViewModel> {


    private String accountName;

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
            accountName = (String) getIntent().getExtras().get(IntentKeyGlobal.ACCOUNT_NAME);
        } catch (Exception e) {
        }
    }

    @Override
    public void initData() {
        viewModel.setAccountName(accountName);
    }
}
