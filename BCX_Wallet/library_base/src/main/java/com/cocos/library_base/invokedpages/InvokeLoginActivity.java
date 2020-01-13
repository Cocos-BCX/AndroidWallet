package com.cocos.library_base.invokedpages;

import android.content.ComponentName;
import android.content.Intent;
import android.databinding.Observable;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.library_base.BR;
import com.cocos.library_base.R;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.base.BaseInvokeModel;
import com.cocos.library_base.base.BaseInvokeResultModel;
import com.cocos.library_base.databinding.ActivityInvokeLoginBinding;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.invokedpages.viewmodel.InvokeLoginViewModel;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocosbcx.invokesdk.dapp_client.model.Authorize;

/**
 * @author ningkang.guo
 * @Date 2020/1/13
 */

@Route(path = RouterActivityPath.ACTIVITY_INVOKE_LOGIN)
public class InvokeLoginActivity extends BaseActivity<ActivityInvokeLoginBinding, InvokeLoginViewModel> {

    private Authorize authorize;
    private BaseInvokeModel baseInvokeModel;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_invoke_login;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        try {
            Bundle bundle = getIntent().getExtras();
            authorize = (Authorize) bundle.getSerializable(IntentKeyGlobal.INVOKE_AUTHORIZE_INFO);
            baseInvokeModel = (BaseInvokeModel) bundle.getSerializable(IntentKeyGlobal.INVOKE_BASE_INFO);
        } catch (Exception e) {
        }
    }

    @Override
    public void initData() {
        viewModel.setAuthorizeData(authorize, baseInvokeModel);
    }


    @Override
    public void initViewObservable() {
        viewModel.uc.invokeLoginCancelObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Log.i("baseInvokeModel", baseInvokeModel.getPackageName());
                Log.i("baseInvokeModel", baseInvokeModel.getClassName());
                ComponentName component = new ComponentName(baseInvokeModel.getPackageName(), baseInvokeModel.getClassName());
                Intent intent = new Intent();
                intent.setComponent(component);
                BaseInvokeResultModel baseInvokeResultModel = new BaseInvokeResultModel();
                baseInvokeResultModel.setCode(0);
                baseInvokeResultModel.setMessage("canceled");
                baseInvokeResultModel.setActionId(authorize.getActionId());
                intent.putExtra("result", GsonSingleInstance.getGsonInstance().toJson(baseInvokeResultModel));
                InvokeLoginActivity.this.startActivity(intent);
                onBackPressed();
            }
        });

        viewModel.uc.invokeLoginConfirmObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                ComponentName component = new ComponentName(baseInvokeModel.getPackageName(), baseInvokeModel.getClassName());
                Intent intent = new Intent();
                intent.setComponent(component);
                BaseInvokeResultModel baseInvokeResultModel = new BaseInvokeResultModel();
                baseInvokeResultModel.setCode(1);
                baseInvokeResultModel.setData(viewModel.invokeLoginAccount.get());
                baseInvokeResultModel.setActionId(authorize.getActionId());
                intent.putExtra("result", GsonSingleInstance.getGsonInstance().toJson(baseInvokeResultModel));
                startActivity(intent);
                onBackPressed();
            }
        });

        viewModel.uc.invokeLoginSwitchObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
