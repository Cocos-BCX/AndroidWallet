package com.cocos.library_base.invokedpages;

import android.app.Application;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.library_base.BR;
import com.cocos.library_base.R;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.component.switch_account.SwitchAccountViewModel;
import com.cocos.library_base.databinding.ActivityInvokeLoginBinding;
import com.cocos.library_base.databinding.DialogSwitchAccountBinding;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.invokedpages.model.Authorize;
import com.cocos.library_base.invokedpages.model.BaseInvokeModel;
import com.cocos.library_base.invokedpages.model.BaseInvokeResultModel;
import com.cocos.library_base.invokedpages.viewmodel.InvokeLoginViewModel;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.DensityUtils;
import com.cocos.library_base.utils.IntentUtils;
import com.cocos.library_base.utils.StatusBarUtils;
import com.cocos.library_base.utils.Utils;

/**
 * @author ningkang.guo
 * @Date 2020/1/13
 */

@Route(path = RouterActivityPath.ACTIVITY_INVOKE_LOGIN)
public class InvokeLoginActivity extends BaseActivity<ActivityInvokeLoginBinding, InvokeLoginViewModel> {

    private Authorize authorize;
    private BaseInvokeModel baseInvokeModel;
    private BottomSheetDialog dialog;

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
        int statusHeight = StatusBarUtils.getStatusBarHeight(Utils.getContext());
        binding.invokeLoginTitle.setPadding(0, statusHeight, DensityUtils.dip2px(Utils.getContext(), 0), 0);
        viewModel.setAuthorizeData(authorize, baseInvokeModel);
    }


    @Override
    public void onHandleEvent(EventBusCarrier busCarrier) {
        if (TextUtils.equals(EventTypeGlobal.DIALOG_DISMISS_TYPE, busCarrier.getEventType())) {
            dialog.dismiss();
        } else if (TextUtils.equals(EventTypeGlobal.SWITCH_ACCOUNT, busCarrier.getEventType())) {
            viewModel.invokeLoginAccount.set(AccountHelperUtils.getCurrentAccountName());
        }
    }


    @Override
    public void initViewObservable() {
        viewModel.uc.invokeLoginCancelObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                finish();
                IntentUtils.jumpToDappWithCancel(InvokeLoginActivity.this, baseInvokeModel, authorize.getActionId());
            }
        });

        viewModel.uc.invokeLoginConfirmObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                finish();
                BaseInvokeResultModel baseInvokeResultModel = new BaseInvokeResultModel();
                baseInvokeResultModel.setCode(1);
                baseInvokeResultModel.setData(viewModel.invokeLoginAccount.get());
                baseInvokeResultModel.setActionId(authorize.getActionId());
                IntentUtils.jumpToDapp(InvokeLoginActivity.this, baseInvokeResultModel, baseInvokeModel);
            }
        });

        viewModel.uc.invokeLoginSwitchObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                dialog = new BottomSheetDialog(InvokeLoginActivity.this);
                DialogSwitchAccountBinding binding = DataBindingUtil.inflate(LayoutInflater.from(Utils.getContext()), R.layout.dialog_switch_account, null, false);
                binding.addAccount.setVisibility(View.INVISIBLE);
                dialog.setContentView(binding.getRoot());
                binding.setViewModel(new SwitchAccountViewModel((Application) Utils.getContext()));
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        IntentUtils.jumpToDappWithCancel(InvokeLoginActivity.this, baseInvokeModel, authorize.getActionId());
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
