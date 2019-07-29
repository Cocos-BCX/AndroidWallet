package com.cocos.module_mine.asset_operate.transfer_nhasset;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.base.BaseVerifyPasswordDialog;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.entity.ContactModel;
import com.cocos.library_base.entity.FeeModel;
import com.cocos.library_base.entity.OperateResultModel;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_mine.BR;
import com.cocos.module_mine.R;
import com.cocos.module_mine.asset_overview.NHAssetDetailActivity;
import com.cocos.module_mine.databinding.ActivityTransferNhAssetBinding;
import com.cocos.module_mine.databinding.DialogTransferNhAssetConfirmBinding;
import com.cocos.module_mine.entity.NHAssetModel;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import io.reactivex.functions.Consumer;

/**
 * @author ningkang.guo
 * @Date 2019/7/17
 */
@Route(path = RouterActivityPath.ACTIVITY_NH_ASSET_TRANSFER)
public class TransferNHAssetActivity extends BaseActivity<ActivityTransferNhAssetBinding, TransferNHAssetViewModel> {

    private NHAssetModel.NHAssetModelBean nHAssetModelBean;
    private BottomSheetDialog dialog;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_transfer_nh_asset;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        try {
            nHAssetModelBean = (NHAssetModel.NHAssetModelBean) Objects.requireNonNull(getIntent().getExtras()).getSerializable(IntentKeyGlobal.NH_ASSET_MODEL);
        } catch (Exception e) {
        }
    }

    @Override
    public void onHandleEvent(EventBusCarrier busCarrier) {
        if (null != busCarrier) {
            if (TextUtils.equals(EventTypeGlobal.SHOW_TRANSFER_NH_ASSET_PASSWORD_VERIFY_DIALOG, busCarrier.getEventType())) {
                dialog.dismiss();
                NHAssetModel.NHAssetModelBean nhAssetModelBean = (NHAssetModel.NHAssetModelBean) busCarrier.getObject();
                showTransferAssetPasswordVerifyDialog(nhAssetModelBean);
            } else if (TextUtils.equals(EventTypeGlobal.DIALOG_DISMISS_TYPE, busCarrier.getEventType())) {
                if (null != dialog) {
                    dialog.dismiss();
                }
            }
        }
    }

    @Override
    public void initData() {
        viewModel.setNhAsset(nHAssetModelBean);
    }

    private void showTransferAssetPasswordVerifyDialog(final NHAssetModel.NHAssetModelBean nhAssetModelBean) {
        final BaseVerifyPasswordDialog passwordVerifyDialog = new BaseVerifyPasswordDialog();
        passwordVerifyDialog.show(getSupportFragmentManager(), "passwordVerifyDialog");
        passwordVerifyDialog.setPasswordListener(new BaseVerifyPasswordDialog.IPasswordListener() {
            @Override
            public void onFinish(String password) {
                CocosBcxApiWrapper.getBcxInstance().transfer_nh_asset(password, nhAssetModelBean.from, nhAssetModelBean.to, "COCOS", nhAssetModelBean.id, new IBcxCallBack() {
                    @Override
                    public void onReceiveValue(String s) {
                        Log.i("transfer_nh_asset", s);
                        final OperateResultModel operateResultModel = GsonSingleInstance.getGsonInstance().fromJson(s, OperateResultModel.class);
                        if (operateResultModel.code == 105) {
                            ToastUtils.showShort(R.string.module_mine_wrong_password);
                            return;
                        }
                        if (!TextUtils.isEmpty(operateResultModel.message)
                                && (operateResultModel.message.contains("insufficient_balance")
                                || operateResultModel.message.contains("Insufficient Balance"))) {
                            ToastUtils.showShort(R.string.insufficient_balance);
                            return;
                        }
                        if (!operateResultModel.isSuccess()) {
                            ToastUtils.showShort(R.string.net_work_failed);
                        }
                        NHAssetDetailActivity.nhAssetDetailActivity.finish();
                        finish();
                        ToastUtils.showShort(R.string.module_mine_transfer_nh_asset_success);
                    }
                });
            }

            @Override
            public void cancel() {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IntentKeyGlobal.REQ_CONTACT_CODE) {
                Bundle bundle = data.getExtras();
                ContactModel contactModel = (ContactModel) bundle.getSerializable(IntentKeyGlobal.CONTACT_ENTITY);
                viewModel.nhAssetReciver.set(contactModel.accountName);
            } else if (requestCode == IntentKeyGlobal.REQ_CAPTURE_CODE) {
                try {
                    Bundle bundle = data.getExtras();
                    String captureResult = bundle.getString(IntentKeyGlobal.CAPTURE_RESULT);
                    JSONObject jsonObject = new JSONObject(captureResult);
                    if (jsonObject.has("address")) {
                        viewModel.nhAssetReciver.set(String.valueOf(jsonObject.get("address")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    @Override
    public void initViewObservable() {
        viewModel.uc.transferNhBtnObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (TextUtils.isEmpty(viewModel.nhAssetReciver.get())) {
                    ToastUtils.showShort(R.string.module_mine_nh_receiver_can_not_be_empty);
                    return;
                }
                if (TextUtils.equals(viewModel.nhAssetReciver.get(), AccountHelperUtils.getCurrentAccountName())) {
                    ToastUtils.showShort(R.string.module_mine_nh_receiver_can_not_be_self);
                    return;
                }
                CocosBcxApiWrapper.getBcxInstance().transfer_nh_asset_fee(AccountHelperUtils.getCurrentAccountName(), viewModel.nhAssetReciver.get(), "COCOS", nHAssetModelBean.id, new IBcxCallBack() {
                    @Override
                    public void onReceiveValue(final String s) {
                        MainHandler.getInstance().post(new Runnable() {
                            @Override
                            public void run() {
                                final FeeModel feeModel = GsonSingleInstance.getGsonInstance().fromJson(s, FeeModel.class);
                                if (!feeModel.isSuccess()) {
                                    ToastUtils.showShort(R.string.net_work_failed);
                                    return;
                                }
                                dialog = new BottomSheetDialog(TransferNHAssetActivity.this);
                                DialogTransferNhAssetConfirmBinding binding = DataBindingUtil.inflate(LayoutInflater.from(Utils.getContext()), R.layout.dialog_transfer_nh_asset_confirm, null, false);
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
                                final TransferNhAssetConfirmViewModel transferNhAssetViewModel = new TransferNhAssetConfirmViewModel(getApplication());
                                binding.setViewModel(transferNhAssetViewModel);
                                nHAssetModelBean.minerFee = feeModel.data.amount;
                                nHAssetModelBean.from = AccountHelperUtils.getCurrentAccountName();
                                nHAssetModelBean.to = viewModel.nhAssetReciver.get();
                                nHAssetModelBean.feeSymbol = "COCOS";
                                transferNhAssetViewModel.setNhAssetModel(nHAssetModelBean);
                                dialog.show();
                            }
                        });
                    }
                });
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
                        navigation(TransferNHAssetActivity.this, IntentKeyGlobal.REQ_CONTACT_CODE);
            }
        });

        viewModel.uc.toCaptureActivity.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @SuppressLint("CheckResult")
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                RxPermissions rxPermissions = new RxPermissions(TransferNHAssetActivity.this);
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
                                            navigation(TransferNHAssetActivity.this, IntentKeyGlobal.REQ_CAPTURE_CODE);
                                }
                            }
                        });
            }
        });
    }

}
