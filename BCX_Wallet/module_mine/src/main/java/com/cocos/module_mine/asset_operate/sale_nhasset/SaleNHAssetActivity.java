package com.cocos.module_mine.asset_operate.sale_nhasset;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
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
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.NumberUtil;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_mine.BR;
import com.cocos.module_mine.R;
import com.cocos.module_mine.databinding.ActivitySaleNhassetBinding;
import com.cocos.module_mine.databinding.DialogSaleNhassetInfoLayoutBinding;
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
        } else if (TextUtils.equals(EventTypeGlobal.SALE_SUCCESS, busCarrier.getEventType())) {
            dialog.dismiss();
            finish();
        }
    }

    @Override
    public void initViewObservable() {
        dialog = new BottomSheetDialog(this);
        DialogSaleNhassetInfoLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(Utils.getContext()), R.layout.dialog_sale_nhasset_info_layout, null, false);
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
                if (TextUtils.isEmpty(viewModel.salePricesAmount.get())) {
                    ToastUtils.showShort(R.string.module_mine_nh_asset_sale_price_hint);
                    return;
                }
                if (TextUtils.isEmpty(viewModel.saleValidTime.get())) {
                    ToastUtils.showShort(R.string.module_mine_nh_asset_sale_valid_time_hint);
                    return;
                }
                if (new BigDecimal(viewModel.salePricesAmount.get()).compareTo(BigDecimal.ZERO) < 0) {
                    ToastUtils.showShort(R.string.module_mine_sale_nh_price_error);
                    return;
                }
                final BaseVerifyPasswordDialog passwordVerifyDialog = new BaseVerifyPasswordDialog();
                passwordVerifyDialog.show(getSupportFragmentManager(), "passwordVerifyDialog");
                passwordVerifyDialog.setPasswordListener(new BaseVerifyPasswordDialog.IPasswordListener() {
                    @Override
                    public void onFinish(final String password) {
                        CocosBcxApiWrapper.getBcxInstance().create_nh_asset_order_fee("otcaccount",
                                AccountHelperUtils.getCurrentAccountName(), nhAssetModelBean.id, viewModel.saleFee.get(),
                                "COCOS", viewModel.saleMemo.get(), viewModel.salePricesAmount.get(),
                                viewModel.salePricesSymbol.get(), Long.valueOf(Objects.requireNonNull(viewModel.saleValidTime.get())), new IBcxCallBack() {
                                    @Override
                                    public void onReceiveValue(final String s) {
                                        MainHandler.getInstance().post(new Runnable() {
                                            @Override
                                            public void run() {
                                                final FeeModel feeModel = GsonSingleInstance.getGsonInstance().fromJson(s, FeeModel.class);
                                                if (!feeModel.isSuccess()) {
                                                    return;
                                                }
                                                SaleNHAssetParamsModel saleNHAssetParamsModel = new SaleNHAssetParamsModel();
                                                saleNHAssetParamsModel.setNhAssetId(nhAssetModelBean.id);
                                                saleNHAssetParamsModel.setPriceAmount(viewModel.salePricesAmount.get());
                                                saleNHAssetParamsModel.setPriceSymbol(viewModel.salePricesSymbol.get());
                                                saleNHAssetParamsModel.setMinerFee(feeModel.data.amount);
                                                saleNHAssetParamsModel.setSaleFee(viewModel.saleFee.get());
                                                saleNHAssetParamsModel.setOrderMemo(viewModel.saleMemo.get());
                                                saleNHAssetParamsModel.setValidTime(viewModel.saleValidTime.get());
                                                saleNHAssetParamsModel.setPassword(password);
                                                orderConfirmViewModel.setSaleInfoData(saleNHAssetParamsModel);
                                                dialog.show();
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
        });
    }
}
