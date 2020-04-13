package com.cocos.library_base.invokedpages;


import android.databinding.Observable;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.bcx_sdk.bcx_wallet.chain.signed_message;
import com.cocos.library_base.BR;
import com.cocos.library_base.R;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.databinding.AcivitySignmessageBinding;
import com.cocos.library_base.entity.PrivateKeyModel;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.invokedpages.model.BaseInvokeModel;
import com.cocos.library_base.invokedpages.model.BaseInvokeResultModel;
import com.cocos.library_base.invokedpages.model.SignMessage;
import com.cocos.library_base.invokedpages.viewmodel.SignMessageViewModel;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.DensityUtils;
import com.cocos.library_base.utils.IntentUtils;
import com.cocos.library_base.utils.StatusBarUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.library_base.widget.JsWebVerifyPasswordDialog;
import com.google.gson.Gson;

import java.util.Map;


@Route(path = RouterActivityPath.ACTIVITY_INVOKE_SIGNMESSAGE)
public class InvokeSignMessageActivity extends BaseActivity<AcivitySignmessageBinding, SignMessageViewModel> {

    private SignMessage signMessage;
    private BaseInvokeModel baseInvokeModel;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.acivity_signmessage;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        try {
            Bundle bundle = getIntent().getExtras();
            signMessage = (SignMessage) bundle.getSerializable(IntentKeyGlobal.INVOKE_SIGNMESSAGE);
            baseInvokeModel = (BaseInvokeModel) bundle.getSerializable(IntentKeyGlobal.INVOKE_BASE_INFO);
        } catch (Exception e) {
        }
    }

    @Override
    public void initData() {
        int statusHeight = StatusBarUtils.getStatusBarHeight(Utils.getContext());
        binding.invokeSignTitle.setPadding(0, statusHeight, DensityUtils.dip2px(Utils.getContext(), 0), 0);
        viewModel.setSignData(signMessage, baseInvokeModel);
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.invokeSignConfirm.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                JsWebVerifyPasswordDialog passwordDialog = new JsWebVerifyPasswordDialog();
                passwordDialog.show(getSupportFragmentManager(), "passwordDialog");
                passwordDialog.setPasswordListener(new JsWebVerifyPasswordDialog.IPasswordListener() {
                    @Override
                    public void onFinish(String password) {
                        signString(password, signMessage.getMessage());
                    }

                    @Override
                    public void cancel() {
                    }
                });
            }
        });

        viewModel.uc.invokeSignCancel.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                finish();
                IntentUtils.jumpToDappWithCancel(InvokeSignMessageActivity.this, baseInvokeModel, signMessage.getActionId());
            }
        });
    }

    private void signString(String password, String signMessages) {
        CocosBcxApiWrapper.getBcxInstance().export_private_key(AccountHelperUtils.getCurrentAccountName(), password, new IBcxCallBack() {
            @Override
            public void onReceiveValue(String value) {
                MainHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final PrivateKeyModel keyModel = GsonSingleInstance.getGsonInstance().fromJson(value, PrivateKeyModel.class);
                            if (keyModel.code == 105) {
                                ToastUtils.showShort(R.string.module_asset_wrong_password);
                                return;
                            }
                            if (!keyModel.isSuccess()) {
                                return;
                            }
                            keyModel.setAccountName(AccountHelperUtils.getCurrentAccountName());
                            Map<String, String> keys = keyModel.getData();
                            BaseInvokeResultModel baseInvokeResultModel = new BaseInvokeResultModel();
                            Gson gson = GsonSingleInstance.getGsonInstance();
                            for (Map.Entry<String, String> public_keys : keys.entrySet()) {
                                if (TextUtils.equals(public_keys.getKey(), AccountHelperUtils.getActivePublicKey(keyModel.getAccountName()))) {
                                    signed_message signed_message = CocosBcxApiWrapper.getBcxInstance().signMessage(public_keys.getValue(), signMessages);
                                    baseInvokeResultModel.setCode(1);
                                    baseInvokeResultModel.setData(gson.toJson(signed_message));
                                    baseInvokeResultModel.setMessage("active");
                                    break;
                                } else {
                                    signed_message signed_message = CocosBcxApiWrapper.getBcxInstance().signMessage(public_keys.getValue(), signMessages);
                                    baseInvokeResultModel.setCode(1);
                                    baseInvokeResultModel.setData(gson.toJson(signed_message));
                                    baseInvokeResultModel.setMessage("owner");
                                }
                            }
                            finish();
                            baseInvokeResultModel.setActionId(signMessage.getActionId());
                            IntentUtils.jumpToDapp(InvokeSignMessageActivity.this, baseInvokeResultModel, baseInvokeModel);
                        } catch (Exception e) {
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            finish();
            IntentUtils.jumpToDappWithCancel(InvokeSignMessageActivity.this, baseInvokeModel, signMessage.getActionId());
        }catch (Exception e){
        }
    }

}
