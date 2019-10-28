package com.cocos.module_mine.asset_overview;

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
import com.cocos.library_base.entity.OperateResultModel;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.module_mine.BR;
import com.cocos.module_mine.R;
import com.cocos.module_mine.asset_operate.delete_nhasset.DeleteNhAssetViewModel;
import com.cocos.module_mine.databinding.ActivityNhAssetDetaiilBinding;
import com.cocos.module_mine.databinding.DialogDeleteNhAssetConfirmBinding;
import com.cocos.module_mine.entity.NHAssetModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author ningkang.guo
 * @Date 2019/7/16
 */
@Route(path = RouterActivityPath.ACTIVITY_NH_ASSET_DETAIL)
public class NHAssetDetailActivity extends BaseActivity<ActivityNhAssetDetaiilBinding, NHAssetDetailViewModel> {

    NHAssetModel.NHAssetModelBean nHAssetModelBean;
    private BottomSheetDialog dialog;
    public static NHAssetDetailActivity nhAssetDetailActivity;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_nh_asset_detaiil;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        try {
            nhAssetDetailActivity = this;
            nHAssetModelBean = (NHAssetModel.NHAssetModelBean) Objects.requireNonNull(getIntent().getExtras()).getSerializable(IntentKeyGlobal.NH_ASSET_MODEL);
        } catch (Exception e) {
        }
    }

    @Override
    public void initData() {
        viewModel.requestAssetDetailData(nHAssetModelBean);
    }

    @Override
    public void onHandleEvent(EventBusCarrier busCarrier) {
        if (null != busCarrier) {
            if (TextUtils.equals(EventTypeGlobal.SHOW_DELETE_NH_ASSET_PASSWORD_VERIFY_DIALOG, busCarrier.getEventType())) {
                dialog.dismiss();
                NHAssetModel.NHAssetModelBean nhAssetModelBean = (NHAssetModel.NHAssetModelBean) busCarrier.getObject();
                showDeleteAssetPasswordVerifyDialog(nhAssetModelBean);
            } else if (TextUtils.equals(EventTypeGlobal.DIALOG_DISMISS_TYPE, busCarrier.getEventType())) {
                if (null != dialog) {
                    dialog.dismiss();
                }
            }
        }
    }

    @Override
    public void initViewObservable() {
        // 删除资产
        viewModel.uc.deteleBtnObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                dialog = new BottomSheetDialog(NHAssetDetailActivity.this);
                DialogDeleteNhAssetConfirmBinding binding = DataBindingUtil.inflate(LayoutInflater.from(Utils.getContext()), R.layout.dialog_delete_nh_asset_confirm, null, false);
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
                final DeleteNhAssetViewModel deleteNhAssetViewModel = new DeleteNhAssetViewModel(getApplication());
                binding.setViewModel(deleteNhAssetViewModel);
                deleteNhAssetViewModel.setNhAssetModel(nHAssetModelBean);
                dialog.show();
            }
        });
        // 转移资产
        viewModel.uc.transferBtnObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(IntentKeyGlobal.NH_ASSET_MODEL, nHAssetModelBean);
                ARouter.getInstance().build(RouterActivityPath.ACTIVITY_NH_ASSET_TRANSFER).with(bundle).navigation();
            }
        });
    }


    private void showDeleteAssetPasswordVerifyDialog(final NHAssetModel.NHAssetModelBean nhAssetModelBean) {
        final BaseVerifyPasswordDialog passwordVerifyDialog = new BaseVerifyPasswordDialog();
        passwordVerifyDialog.show(getSupportFragmentManager(), "passwordVerifyDialog");
        passwordVerifyDialog.setPasswordListener(new BaseVerifyPasswordDialog.IPasswordListener() {
            @Override
            public void onFinish(String password) {
                List<String> nhAsset_ids = new ArrayList<>();
                nhAsset_ids.add(nhAssetModelBean.id);
                CocosBcxApiWrapper.getBcxInstance().delete_nh_asset(AccountHelperUtils.getCurrentAccountName(), password, nhAsset_ids, new IBcxCallBack() {
                    @Override
                    public void onReceiveValue(String s) {
                        Log.i("delete_nh_asset", s);
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
                            return;
                        }
                        finish();
                        ToastUtils.showShort(R.string.module_mine_delete_nh_asset_success);
                    }
                });
            }

            @Override
            public void cancel() {

            }
        });
    }
}
