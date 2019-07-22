package com.cocos.module_mine.asset_operate.transfer_nhasset;

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
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.base.BaseVerifyPasswordDialog;
import com.cocos.library_base.bus.event.EventBusCarrier;
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

import java.util.Objects;

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
                        if (null == operateResultModel) {
                            ToastUtils.showShort(R.string.net_work_failed);
                            return;
                        }
                        if (operateResultModel.code == 105) {
                            ToastUtils.showShort(R.string.module_mine_wrong_password);
                            return;
                        }
                        if (operateResultModel.isSuccess()) {
                            NHAssetDetailActivity.nhAssetDetailActivity.finish();
                            finish();
                            ToastUtils.showShort(R.string.module_mine_transfer_nh_asset_success);
                        }
                    }
                });
            }

            @Override
            public void cancel() {

            }
        });

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
    }

}
