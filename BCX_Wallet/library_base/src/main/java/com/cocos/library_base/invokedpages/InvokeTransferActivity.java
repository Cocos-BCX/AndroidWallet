package com.cocos.library_base.invokedpages;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.library_base.BR;
import com.cocos.library_base.R;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.base.BaseVerifyPasswordDialog;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.component.transfer.OrderConfirmViewModel;
import com.cocos.library_base.databinding.ActivityInvokeTransferBinding;
import com.cocos.library_base.databinding.DialogTransferPayConfirmBinding;
import com.cocos.library_base.entity.TransferModel;
import com.cocos.library_base.entity.TransferParamsModel;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.invokedpages.model.BaseInvokeModel;
import com.cocos.library_base.invokedpages.model.BaseInvokeResultModel;
import com.cocos.library_base.invokedpages.model.Transfer;
import com.cocos.library_base.invokedpages.viewmodel.InvokeTransferViewModel;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.DensityUtils;
import com.cocos.library_base.utils.IntentUtils;
import com.cocos.library_base.utils.StatusBarUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;

import java.text.NumberFormat;

/**
 * @author ningkang.guo
 * @Date 2020/1/13
 */

@Route(path = RouterActivityPath.ACTIVITY_INVOKE_TRANSFER)
public class InvokeTransferActivity extends BaseActivity<ActivityInvokeTransferBinding, InvokeTransferViewModel> {

    private Transfer transferInvokeModel;
    private BaseInvokeModel baseInvokeModel;
    private BottomSheetDialog dialog;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_invoke_transfer;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        try {
            Bundle bundle = getIntent().getExtras();
            transferInvokeModel = (Transfer) bundle.getSerializable(IntentKeyGlobal.INVOKE_TRANSFER_INFO);
            baseInvokeModel = (BaseInvokeModel) bundle.getSerializable(IntentKeyGlobal.INVOKE_BASE_INFO);
        } catch (Exception e) {
        }
    }

    @Override
    public void initData() {
        int statusHeight = StatusBarUtils.getStatusBarHeight(Utils.getContext());
        binding.invokeLoginTitle.setPadding(0, statusHeight, DensityUtils.dip2px(Utils.getContext(), 0), 0);
        viewModel.setTransferData(transferInvokeModel);
    }


    @Override
    public void onHandleEvent(EventBusCarrier busCarrier) {
        if (TextUtils.equals(EventTypeGlobal.DIALOG_DISMISS_TYPE, busCarrier.getEventType())) {
            dialog.dismiss();
        } else if (TextUtils.equals(EventTypeGlobal.SHOW_TRANSFER_PASSWORD_VERIFY_DIALOG, busCarrier.getEventType())) {
            dialog.dismiss();
            final TransferParamsModel transferParamsModel = (TransferParamsModel) busCarrier.getObject();
            final BaseVerifyPasswordDialog passwordVerifyDialog = new BaseVerifyPasswordDialog();
            passwordVerifyDialog.show(getSupportFragmentManager(), "passwordVerifyDialog");
            passwordVerifyDialog.setPasswordListener(new BaseVerifyPasswordDialog.IPasswordListener() {
                @Override
                public void onFinish(final String password) {
                    CocosBcxApiWrapper.getBcxInstance().transfer(password, transferParamsModel.getAccountName(), transferParamsModel.getReceivablesAccountName(), transferParamsModel.getTransferAmount(),
                            transferParamsModel.getTransferSymbol(), transferParamsModel.getTransferMemo(), false, new IBcxCallBack() {
                                @Override
                                public void onReceiveValue(final String s) {
                                    MainHandler.getInstance().post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Log.i("transfer", s);
                                                TransferModel baseResult = GsonSingleInstance.getGsonInstance().fromJson(s, TransferModel.class);
                                                if (baseResult.message.contains("Insufficient Balance")) {
                                                    ToastUtils.showShort(R.string.module_asset_operate_fee_not_much);
                                                    return;
                                                }
                                                if (baseResult.code == 105) {
                                                    ToastUtils.showShort(R.string.module_asset_wrong_password);
                                                    return;
                                                }
                                                if (baseResult.code == 112) {
                                                    ToastUtils.showShort(R.string.module_asset_private_key_author_failed);
                                                    return;
                                                }

                                                if (baseResult.code == 104) {
                                                    ToastUtils.showShort(R.string.module_asset_account_not_found);
                                                    return;
                                                }
                                                if (!baseResult.isSuccess()) {
                                                    ToastUtils.showShort(R.string.net_work_failed);
                                                    return;
                                                }
                                                ToastUtils.showShort(R.string.module_asset_transfer_success);
                                                finish();
                                                BaseInvokeResultModel baseInvokeResultModel = new BaseInvokeResultModel();
                                                baseInvokeResultModel.setCode(1);
                                                baseInvokeResultModel.setData(baseResult.data);
                                                baseInvokeResultModel.setActionId(transferInvokeModel.getActionId());
                                                IntentUtils.jumpToDapp(InvokeTransferActivity.this, baseInvokeResultModel, baseInvokeModel);
                                            } catch (Exception e) {
                                                ToastUtils.showShort(R.string.net_work_failed);
                                            }
                                        }
                                    });
                                }
                            });
                }

                @Override
                public void cancel() {

                }
            });
        }
    }


    @Override
    public void initViewObservable() {
        dialog = new BottomSheetDialog(InvokeTransferActivity.this);
        DialogTransferPayConfirmBinding binding = DataBindingUtil.inflate(LayoutInflater.from(Utils.getContext()), R.layout.dialog_transfer_pay_confirm, null, false);
        dialog.setContentView(binding.getRoot());
        // 设置dialog 完全显示
        View parent = (View) binding.getRoot().getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        binding.getRoot().measure(0, 0);
        behavior.setPeekHeight(binding.getRoot().getMeasuredHeight());
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) parent.getLayoutParams();
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        parent.setLayoutParams(params);
        dialog.setCanceledOnTouchOutside(false);
        final OrderConfirmViewModel orderConfirmViewModel = new OrderConfirmViewModel(getApplication());
        binding.setViewModel(orderConfirmViewModel);
        viewModel.uc.invokeTransferConfirmObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (transferInvokeModel.getExpired() > 0 && System.currentTimeMillis() > transferInvokeModel.getExpired()) {
                    finish();
                    IntentUtils.jumpToDappWithError(InvokeTransferActivity.this, baseInvokeModel, transferInvokeModel.getActionId(), "request expired!");
                    return;
                }

                if (TextUtils.isEmpty(transferInvokeModel.getFrom())) {
                    ToastUtils.showShort(R.string.transferAccountName_empty);
                    return;
                }

                if (TextUtils.isEmpty(transferInvokeModel.getTo())) {
                    ToastUtils.showShort(R.string.module_asset_receivablesAccountName_empty);
                    return;
                }

                if (TextUtils.isEmpty(String.valueOf(transferInvokeModel.getAmount()))) {
                    ToastUtils.showShort(R.string.module_asset_transfer_amount_empty);
                    return;
                }

                if (TextUtils.equals(transferInvokeModel.getFrom(), transferInvokeModel.getTo())) {
                    ToastUtils.showShort(R.string.module_asset_transfer_account_can_not_yourself);
                    return;
                }
                NumberFormat instance = NumberFormat.getInstance();
                instance.setGroupingUsed(false);
                instance.setMaximumFractionDigits(transferInvokeModel.getPrecision());
                TransferParamsModel transferParamsModel = new TransferParamsModel();
                transferParamsModel.setAccountName(transferInvokeModel.getFrom());
                transferParamsModel.setReceivablesAccountName(transferInvokeModel.getTo());
                transferParamsModel.setTransferAmount(instance.format(transferInvokeModel.getAmount()));
                transferParamsModel.setTransferMemo(transferInvokeModel.getMemo());
                transferParamsModel.setTransferSymbol(transferInvokeModel.getSymbol());
                orderConfirmViewModel.setTransferInfoData(transferParamsModel);
                dialog.show();
            }
        });

        viewModel.uc.invokeTransferCancelObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                finish();
                IntentUtils.jumpToDappWithCancel(InvokeTransferActivity.this, baseInvokeModel, transferInvokeModel.getActionId());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        IntentUtils.jumpToDappWithCancel(InvokeTransferActivity.this, baseInvokeModel, transferInvokeModel.getActionId());
    }
}
