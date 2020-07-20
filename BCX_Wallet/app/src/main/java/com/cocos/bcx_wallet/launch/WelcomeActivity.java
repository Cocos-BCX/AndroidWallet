package com.cocos.bcx_wallet.launch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.bcx_sdk.bcx_log.LogUtils;
import com.cocos.bcx_wallet.BR;
import com.cocos.bcx_wallet.R;
import com.cocos.bcx_wallet.databinding.ActivityWelcomeBinding;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.invokedpages.model.BaseInvokeModel;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.SPUtils;

/**
 * @author ningkang.guo
 * @Date 2019/1/29
 */
public class WelcomeActivity extends BaseActivity<ActivityWelcomeBinding, BaseViewModel> {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_welcome;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }


    @SuppressLint("CheckResult")
    @Override
    public void initData() {

        initIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        initIntent(intent);
    }

    private void initIntent(Intent intent) {
        try {
//            Bundle bundle = intent.getExtras();
//            BaseInvokeModel baseInvokeModel = new BaseInvokeModel();
//            if (bundle != null) {
//                baseInvokeModel.setPackageName(bundle.getString("packageName"));
//                baseInvokeModel.setClassName(bundle.getString("className"));
//                baseInvokeModel.setAppName(bundle.getString("appName"));
//                baseInvokeModel.setAction(bundle.getString("action"));
//            } else {
//                bundle = new Bundle();
//            }
//            Uri uri = intent.getData();
//            if (uri != null) {
//                baseInvokeModel.setParam(uri.getQueryParameter("param"));
//            }
////            Bundle bundle = new Bundle();
//            SPUtils.putBoolean(this, "loadComplete", true);
//            bundle.putSerializable(IntentKeyGlobal.INVOKE_SENDER_INFO, baseInvokeModel);
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_MAIN_PATH).navigation();
            finish();
        } catch (Exception e) {
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_MAIN_PATH).navigation();
            finish();
        }
    }
}
