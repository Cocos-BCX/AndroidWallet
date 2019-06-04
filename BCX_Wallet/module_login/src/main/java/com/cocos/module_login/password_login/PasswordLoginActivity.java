package com.cocos.module_login.password_login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.library_base.BR;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.VersionUtil;
import com.cocos.module_login.R;
import com.cocos.module_login.databinding.ActivityPasswordLoginBinding;
import com.cocos.module_login.register.RegisterActivity;

/**
 * @author ningkang.guo
 * @Date 2019/3/29
 */
@Route(path = RouterActivityPath.ACTIVITY_PASSWORD_LOGIN)
public class PasswordLoginActivity extends BaseActivity<ActivityPasswordLoginBinding, PasswordLoginViewModel> {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_password_login;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void addToContainer() {
        //空实现,不加入ActivityContainer
    }

    @Override
    public void initData() {
        VersionUtil.updateVersion(viewModel, PasswordLoginActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10086) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                boolean hasInstallPermission = getPackageManager().canRequestPackageInstalls();
                if (hasInstallPermission && !TextUtils.isEmpty(VersionUtil.getDestFileDir(PasswordLoginActivity.this))) {
                    VersionUtil.installApkO(PasswordLoginActivity.this, VersionUtil.getDestFileDir(PasswordLoginActivity.this));
                }
            }
        }
    }
}
