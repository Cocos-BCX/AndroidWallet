package com.cocos.module_asset.create;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.module_asset.BR;
import com.cocos.module_asset.R;
import com.cocos.module_asset.databinding.ActivityCreateAccountBinding;



/**
 * @author ningkang.guo
 * @Date 2019/1/31
 */

@Route(path = RouterActivityPath.ACTIVITY_CREATE_ACCOUNT)
public class CreateAccountActivity extends BaseActivity<ActivityCreateAccountBinding, CreateAccountViewModel> {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_create_account;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}
