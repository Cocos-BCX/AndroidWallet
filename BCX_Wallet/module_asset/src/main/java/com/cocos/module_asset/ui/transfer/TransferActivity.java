package com.cocos.module_asset.ui.transfer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.base.BaseVerifyPasswordDialog;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.entity.AssetsModel;
import com.cocos.library_base.entity.ContactModel;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.DecimalDigitsInputFilter;
import com.cocos.library_base.utils.NumberUtil;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_asset.BR;
import com.cocos.module_asset.R;
import com.cocos.module_asset.databinding.ActivityTransferBinding;
import com.cocos.module_asset.databinding.DialogTransferPayConfirmBinding;
import com.cocos.module_asset.entity.TransferModel;
import com.cocos.module_asset.entity.TransferParamsModel;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import io.reactivex.functions.Consumer;

/**
 * @author ningkang.guo
 * @Date 2019/2/18
 */
@Route(path = RouterActivityPath.ACTIVITY_TRANSFER)
public class TransferActivity extends BaseActivity<ActivityTransferBinding, TransferViewModel> {

    private String accountName = AccountHelperUtils.getCurrentAccountName();
    private BottomSheetDialog dialog;
    private AssetsModel.AssetModel assetModel;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_transfer;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        try {
            assetModel = (AssetsModel.AssetModel) getIntent().getExtras().getSerializable(IntentKeyGlobal.ASSET_MODEL);
        } catch (Exception e) {
        }
    }


    @Override
    public void initData() {
        viewModel.setTransferAssetModel(assetModel);
        binding.edtAmount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(assetModel.precision)});
        NumberUtil.setPricePoint1(binding.edtAmount);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IntentKeyGlobal.REQ_CONTACT_CODE) {
                Bundle bundle = data.getExtras();
                ContactModel contactModel = (ContactModel) bundle.getSerializable(IntentKeyGlobal.CONTACT_ENTITY);
                viewModel.receivablesAccountName.set(contactModel.accountName);
            } else if (requestCode == IntentKeyGlobal.REQ_CAPTURE_CODE) {
                try {
                    Bundle bundle = data.getExtras();
                    String captureResult = bundle.getString(IntentKeyGlobal.CAPTURE_RESULT);
                    JSONObject jsonObject = new JSONObject(captureResult);
                    if (jsonObject.has("address")) {
                        viewModel.receivablesAccountName.set(String.valueOf(jsonObject.get("address")));
                        viewModel.transferAmount.set(String.valueOf(jsonObject.get("amount")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void initViewObservable() {
        dialog = new BottomSheetDialog(this);
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
        viewModel.uc.transferBtnObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (TextUtils.isEmpty(viewModel.receivablesAccountName.get())) {
                    ToastUtils.showShort(R.string.module_asset_receivablesAccountName_empty);
                    return;
                }

                if (TextUtils.isEmpty(viewModel.transferAmount.get())) {
                    ToastUtils.showShort(R.string.module_asset_transfer_amount_empty);
                    return;
                }

                if (TextUtils.equals(accountName, viewModel.receivablesAccountName.get())) {
                    ToastUtils.showShort(R.string.module_asset_transfer_account_can_not_yourself);
                    return;
                }

                final BigDecimal transferAmount = new BigDecimal(viewModel.transferAmount.get());
                TransferParamsModel transferParamsModel = new TransferParamsModel();
                transferParamsModel.setAccountName(accountName);
                transferParamsModel.setReceivablesAccountName(viewModel.receivablesAccountName.get());
                transferParamsModel.setAccountBalance(viewModel.accountBalance.get());
                transferParamsModel.setTransferAmount(String.valueOf(transferAmount.add(BigDecimal.ZERO)));
                transferParamsModel.setTransferMemo(viewModel.transferMemo.get());
                transferParamsModel.setTransferSymbol(assetModel.symbol);
                orderConfirmViewModel.setTransferInfoData(transferParamsModel);
                dialog.show();
            }
        });

        viewModel.uc.toContact.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Bundle bundle = new Bundle();
                bundle.putInt(IntentKeyGlobal.TRANSFER_TO_CONTACT, IntentKeyGlobal.GET_CONTACT);
                ARouter.getInstance().
                        build(RouterActivityPath.ACTIVITY_CONTACT).
                        with(bundle).
                        navigation(TransferActivity.this, IntentKeyGlobal.REQ_CONTACT_CODE);
            }
        });

        viewModel.uc.toCaptureActivity.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @SuppressLint("CheckResult")
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                RxPermissions rxPermissions = new RxPermissions(TransferActivity.this);
                rxPermissions.request(Manifest.permission.CAMERA)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) {
                                if (aBoolean) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(IntentKeyGlobal.TO_CAPTURE, IntentKeyGlobal.GET_CAPTURE_RESULT);
                                    ARouter.getInstance().
                                            build(RouterActivityPath.ACTIVITY_CAPTURE).
                                            with(bundle).
                                            navigation(TransferActivity.this, IntentKeyGlobal.REQ_CAPTURE_CODE);
                                }
                            }
                        });
            }
        });
    }


}
