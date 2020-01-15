package com.cocos.library_base.invokedpages;

import android.databinding.Observable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.library_base.BR;
import com.cocos.library_base.R;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.base.BaseVerifyPasswordDialog;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.databinding.ActivityInvokeContractBinding;
import com.cocos.library_base.entity.BaseResultModel;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.invokedpages.model.BaseInvokeModel;
import com.cocos.library_base.invokedpages.model.BaseInvokeResultModel;
import com.cocos.library_base.invokedpages.model.Contract;
import com.cocos.library_base.invokedpages.viewmodel.InvokeContractViewModel;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.DensityUtils;
import com.cocos.library_base.utils.IntentUtils;
import com.cocos.library_base.utils.StatusBarUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;

/**
 * @author ningkang.guo
 * @Date 2020/1/13
 */

@Route(path = RouterActivityPath.ACTIVITY_INVOKE_CONTRACT)
public class InvokeContractActivity extends BaseActivity<ActivityInvokeContractBinding, InvokeContractViewModel> {

    private Contract contract;
    private BaseInvokeModel baseInvokeModel;
    private BottomSheetDialog dialog;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_invoke_contract;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        try {
            Bundle bundle = getIntent().getExtras();
            contract = (Contract) bundle.getSerializable(IntentKeyGlobal.INVOKE_CONTRACT_INFO);
            baseInvokeModel = (BaseInvokeModel) bundle.getSerializable(IntentKeyGlobal.INVOKE_BASE_INFO);
        } catch (Exception e) {
        }
    }

    @Override
    public void initData() {
        int statusHeight = StatusBarUtils.getStatusBarHeight(Utils.getContext());
        binding.invokeLoginTitle.setPadding(0, statusHeight, DensityUtils.dip2px(Utils.getContext(), 0), 0);
        viewModel.setAuthorizeData(contract);
    }


    @Override
    public void onHandleEvent(EventBusCarrier busCarrier) {
        if (TextUtils.equals(EventTypeGlobal.DIALOG_DISMISS_TYPE, busCarrier.getEventType())) {
            dialog.dismiss();
        }
    }


    @Override
    public void initViewObservable() {
        viewModel.uc.invokeContractCancelObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                finish();
                IntentUtils.jumpToDappWithCancel(InvokeContractActivity.this, baseInvokeModel, contract.getActionId());
            }
        });

        viewModel.uc.invokeContractConfirmObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (contract.getExpired() > 0 && System.currentTimeMillis() > contract.getExpired()) {
                    finish();
                    IntentUtils.jumpToDappWithExpirted(InvokeContractActivity.this, baseInvokeModel, contract.getActionId());
                    return;
                }
                BaseVerifyPasswordDialog passwordDialog = new BaseVerifyPasswordDialog();
                passwordDialog.show(getSupportFragmentManager(), "passwordDialog");
                passwordDialog.setPasswordListener(new BaseVerifyPasswordDialog.IPasswordListener() {
                    @Override
                    public void onFinish(String password) {
                        CocosBcxApiWrapper.getBcxInstance().invoking_contract(contract.getAuthorizedAccount(), password, contract.getContractNameOrId(), contract.getFunctionName(), contract.getValueList(), new IBcxCallBack() {
                            @Override
                            public void onReceiveValue(String s) {
                                BaseResultModel<String> baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResultModel.class);
                                finish();
                                BaseInvokeResultModel baseInvokeResultModel = new BaseInvokeResultModel();
                                if (baseResult.getCode() == 105) {
                                    ToastUtils.showShort(R.string.module_asset_wrong_password);
                                    return;
                                }
                                if (baseResult.isSuccess()) {
                                    baseInvokeResultModel.setCode(1);
                                    baseInvokeResultModel.setData(baseResult.getData());
                                    baseInvokeResultModel.setActionId(contract.getActionId());
                                } else {
                                    baseInvokeResultModel.setCode(2);
                                    baseInvokeResultModel.setMessage(baseResult.getMessage());
                                    baseInvokeResultModel.setActionId(contract.getActionId());
                                }
                                IntentUtils.jumpToDapp(InvokeContractActivity.this, baseInvokeResultModel, baseInvokeModel);
                            }
                        });
                    }

                    @Override
                    public void cancel() {
                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        IntentUtils.jumpToDappWithCancel(InvokeContractActivity.this, baseInvokeModel, contract.getActionId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
