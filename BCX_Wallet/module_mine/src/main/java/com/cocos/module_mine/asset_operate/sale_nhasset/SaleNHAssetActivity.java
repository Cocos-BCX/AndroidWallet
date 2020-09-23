package com.cocos.module_mine.asset_operate.sale_nhasset;

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
import com.cocos.library_base.entity.OperateResultModel;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.global.SPKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.NumberUtil;
import com.cocos.library_base.utils.SPUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_mine.BR;
import com.cocos.module_mine.R;
import com.cocos.module_mine.databinding.ActivitySaleNhassetBinding;
import com.cocos.module_mine.databinding.DialogSaleNhassetConfirmLayoutBinding;
import com.cocos.module_mine.entity.NHAssetModel;
import com.cocos.module_mine.entity.SaleNHAssetParamsModel;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author ningkang.guo
 * @Date 2019/7/17
 */

@Route(path = RouterActivityPath.ACTIVITY_SALE_NH_ASSET)
public class SaleNHAssetActivity extends BaseActivity<ActivitySaleNhassetBinding, SaleNHAssetViewModel> {

    NHAssetModel.NHAssetModelBean nhAssetModelBean;
    private BottomSheetDialog dialog;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_sale_nhasset;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        try {
            nhAssetModelBean = (NHAssetModel.NHAssetModelBean) Objects.requireNonNull(getIntent().getExtras()).getSerializable(IntentKeyGlobal.NH_ASSET_MODEL);
        } catch (Exception e) {
        }
    }

    @Override
    public void initData() {
        viewModel.setNhAssetId(nhAssetModelBean);
        NumberUtil.setPricePoint1(binding.edtPriceAmount);
    }

    @Override
    public void onHandleEvent(EventBusCarrier busCarrier) {
        if (TextUtils.equals(EventTypeGlobal.DIALOG_DISMISS_TYPE, busCarrier.getEventType())) {
            dialog.dismiss();
        } else if (TextUtils.equals(EventTypeGlobal.SHOW_SALE_NH_ASSET_PASSWORD_VERIFY_DIALOG, busCarrier.getEventType())) {
            dialog.dismiss();
            SaleNHAssetParamsModel saleAssetParamsModel = (SaleNHAssetParamsModel) busCarrier.getObject();
            showSaleAssetPasswordVerifyDialog(saleAssetParamsModel);
        }
    }

    private void showSaleAssetPasswordVerifyDialog(SaleNHAssetParamsModel saleAssetParamsModel) {
        final BaseVerifyPasswordDialog passwordVerifyDialog = new BaseVerifyPasswordDialog();
        passwordVerifyDialog.show(getSupportFragmentManager(), "passwordVerifyDialog");
        passwordVerifyDialog.setPasswordListener(new BaseVerifyPasswordDialog.IPasswordListener() {
            @Override
            public void onFinish(final String password) {
                try {
                    Log.i("saleMemo", saleAssetParamsModel.getOrderMemo());
                    Double aDouble = Double.valueOf(saleAssetParamsModel.getPriceAmount()) * 0;
                    CocosBcxApiWrapper.getBcxInstance().create_nh_asset_order("otcaccount", AccountHelperUtils.getCurrentAccountName(),
                            password, saleAssetParamsModel.getNhAssetId(), aDouble.toString(),
                            "COCOS", saleAssetParamsModel.getOrderMemo(), saleAssetParamsModel.getPriceAmount(),
                            saleAssetParamsModel.getPriceSymbol(), saleAssetParamsModel.getValidTime(), new IBcxCallBack() {
                                @Override
                                public void onReceiveValue(final String s) {
                                    Log.i("create_nh_asset_order", s);
                                    MainHandler.getInstance().post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                final OperateResultModel feeModel = GsonSingleInstance.getGsonInstance().fromJson(s, OperateResultModel.class);
                                                if (feeModel.code == 105) {
                                                    ToastUtils.showShort(R.string.module_mine_wrong_password);
                                                    return;
                                                }
                                                if (!TextUtils.isEmpty(feeModel.message)
                                                        && (feeModel.message.contains("insufficient_balance")
                                                        || feeModel.message.contains("Insufficient Balance"))) {
                                                    ToastUtils.showShort(R.string.insufficient_balance);
                                                    return;
                                                }
                                                if (!feeModel.isSuccess()) {
                                                    ToastUtils.showShort(R.string.net_work_failed);
                                                    return;
                                                }
                                                ToastUtils.showShort(R.string.module_mine_sale_nh_success);
                                                dialog.dismiss();
                                                finish();
                                            } catch (Exception e) {
                                                ToastUtils.showShort(R.string.net_work_failed);
                                            }
                                        }
                                    });
                                }
                            });
                } catch (Exception e) {

                }
            }

            @Override
            public void cancel() {

            }
        });
    }


    @Override
    public void initViewObservable() {
        dialog = new BottomSheetDialog(this);
        DialogSaleNhassetConfirmLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(Utils.getContext()), R.layout.dialog_sale_nhasset_confirm_layout, null, false);
        dialog.setContentView(binding.getRoot());
        View parent = (View) binding.getRoot().getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        binding.getRoot().measure(0, 0);
        behavior.setPeekHeight(binding.getRoot().getMeasuredHeight());
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) parent.getLayoutParams();
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        parent.setLayoutParams(params);
        dialog.setCanceledOnTouchOutside(false);
        final SaleNhConfirmViewModel orderConfirmViewModel = new SaleNhConfirmViewModel(getApplication());
        binding.setViewModel(orderConfirmViewModel);
        viewModel.uc.saleNHNBtnObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                try {
                    if (TextUtils.isEmpty(viewModel.salePricesAmount.get())) {
                        ToastUtils.showShort(R.string.module_mine_nh_asset_sale_price_hint);
                        return;
                    }
                    if (TextUtils.isEmpty(viewModel.saleValidTime.get())) {
                        ToastUtils.showShort(R.string.module_mine_nh_asset_sale_valid_time_hint);
                        return;
                    }

                    if (TextUtils.equals(viewModel.saleValidTime.get(), "0")) {
                        ToastUtils.showShort(R.string.module_mine_nh_asset_sale_valid_time_less_than_one);
                        return;
                    }

                    if (Long.valueOf(Objects.requireNonNull(viewModel.saleValidTime.get())) > viewModel.saleValidTimeMax) {
                        viewModel.saleValidTime.set(String.valueOf(viewModel.saleValidTimeMax));
                        return;
                    }

                    if (new BigDecimal(viewModel.salePricesAmount.get()).compareTo(BigDecimal.ZERO) < 0) {
                        ToastUtils.showShort(R.string.module_mine_sale_nh_price_error);
                        return;
                    }
                    SaleNHAssetParamsModel saleNHAssetParamsModel = new SaleNHAssetParamsModel();
                    saleNHAssetParamsModel.setNhAssetId(nhAssetModelBean.id);
                    saleNHAssetParamsModel.setPriceAmount(viewModel.salePricesAmount.get());
                    saleNHAssetParamsModel.setPriceSymbol(viewModel.salePricesSymbol.get());
                    saleNHAssetParamsModel.setOrderMemo(viewModel.saleMemo.get());
                    saleNHAssetParamsModel.setValidTime(Long.parseLong(viewModel.saleValidTime.get()));
                    orderConfirmViewModel.setSaleInfoData(saleNHAssetParamsModel);
                    dialog.show();
                } catch (Exception e) {
                }
            }
        });

        viewModel.uc.choosePriceSymbolObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Bundle bundle = new Bundle();
                bundle.putInt(IntentKeyGlobal.SALE_TO_SYMBOL_SELECT, IntentKeyGlobal.GET_CONTACT);
                ARouter.getInstance().
                        build(RouterActivityPath.ACTIVITY_SYMBOL_LIST).
                        with(bundle).
                        navigation(SaleNHAssetActivity.this, IntentKeyGlobal.REQ_SYMBOL_SELECT_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IntentKeyGlobal.REQ_SYMBOL_SELECT_CODE) {
                Bundle bundle = data.getExtras();
                String salePricesSymbol = Objects.requireNonNull(bundle).getString(IntentKeyGlobal.PRICE_SYMBOL);
                viewModel.salePricesSymbol.set(salePricesSymbol);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPUtils.putString(Utils.getContext(), SPKeyGlobal.SYMBOL_SELECTED, "");
    }
}
