package com.cocos.module_asset.nh_order_manager;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.cocos.library_base.entity.NhAssetOrderEntity;
import com.cocos.library_base.entity.OperateResultModel;
import com.cocos.library_base.entity.TabEntity;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_asset.BR;
import com.cocos.module_asset.R;
import com.cocos.module_asset.databinding.ActivityOrderManageBinding;
import com.cocos.module_asset.databinding.DialogBuyOrderConfirmBinding;
import com.cocos.module_asset.databinding.DialogCancelOrderConfirmBinding;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

/**
 * @author ningkang.guo
 * @Date 2019/7/15
 */
@Route(path = RouterActivityPath.ACTIVITY_ORDER_MANAGE)
public class OrderManageActivity extends BaseActivity<ActivityOrderManageBinding, OrderManageViewModel> {

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private int[] mTitles = {R.string.module_asset_nh_order_mine_title, R.string.module_asset_nh_order_all_title,};

    private ArrayList<Fragment> mFragments;
    private BottomSheetDialog dialog;
    private MineNhOrderFragment mineNhOrderFragment;
    private AllNhOrderFragment allNhOrderFragment;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_order_manage;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        mFragments = new ArrayList<>();
        mineNhOrderFragment = new MineNhOrderFragment();
        allNhOrderFragment = new AllNhOrderFragment();
        mFragments.add(mineNhOrderFragment);
        mFragments.add(allNhOrderFragment);
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(Utils.getString(mTitles[i])));
        }
        binding.viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        initTabListener();
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Utils.getString(mTitles[position]);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    @Override
    public void onHandleEvent(EventBusCarrier busCarrier) {
        if (null != busCarrier) {
            if (TextUtils.equals(EventTypeGlobal.SHOW_CANCEL_ORDER_CONFIRM_DIALOG, busCarrier.getEventType())) {
                final NhAssetOrderEntity.NhOrderBean nhOrderBean = (NhAssetOrderEntity.NhOrderBean) busCarrier.getObject();
                CocosBcxApiWrapper.getBcxInstance().cancel_nh_asset_order_fee(nhOrderBean.seller, nhOrderBean.id, "COCOS", new IBcxCallBack() {
                    @Override
                    public void onReceiveValue(final String s) {
                        MainHandler.getInstance().post(new Runnable() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void run() {
                                if (TextUtils.isEmpty(s)) {
                                    ToastUtils.showShort(R.string.net_work_failed);
                                    return;
                                }
                                Log.i("cancel_nh_asset_order_fee", s);
                                final FeeModel feeModel = GsonSingleInstance.getGsonInstance().fromJson(s, FeeModel.class);
                                if (!feeModel.isSuccess()) {
                                    return;
                                }
                                dialog = new BottomSheetDialog(OrderManageActivity.this);
                                DialogCancelOrderConfirmBinding binding = DataBindingUtil.inflate(LayoutInflater.from(Utils.getContext()), R.layout.dialog_cancel_order_confirm, null, false);
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
                                final CancelOrderConfirmViewModel cancelOrderConfirmViewModel = new CancelOrderConfirmViewModel(getApplication());
                                binding.setViewModel(cancelOrderConfirmViewModel);
                                nhOrderBean.minerFee = feeModel.data.amount;
                                nhOrderBean.feeSymbol = "COCOS";
                                cancelOrderConfirmViewModel.setCancelOrderModel(nhOrderBean);
                                dialog.show();
                            }
                        });
                    }
                });
            } else if (TextUtils.equals(EventTypeGlobal.SHOW_BUY_ORDER_CONFIRM_DIALOG, busCarrier.getEventType())) {
                final NhAssetOrderEntity.NhOrderBean nhOrderBean = (NhAssetOrderEntity.NhOrderBean) busCarrier.getObject();
                if (TextUtils.equals(AccountHelperUtils.getCurrentAccountName(), nhOrderBean.sellerName)) {
                    ToastUtils.showShort(R.string.module_asset_can_not_buy_owner_order);
                    return;
                }
                CocosBcxApiWrapper.getBcxInstance().buy_nh_asset_fee(AccountHelperUtils.getCurrentAccountName(), nhOrderBean.id, new IBcxCallBack() {
                    @Override
                    public void onReceiveValue(final String s) {
                        MainHandler.getInstance().post(new Runnable() {
                            @Override
                            public void run() {
                                if (TextUtils.isEmpty(s)) {
                                    ToastUtils.showShort(R.string.net_work_failed);
                                    return;
                                }
                                Log.i("buy_nh_asset_fee", s);
                                final FeeModel feeModel = GsonSingleInstance.getGsonInstance().fromJson(s, FeeModel.class);
                                if (!feeModel.isSuccess()) {
                                    return;
                                }
                                dialog = new BottomSheetDialog(OrderManageActivity.this);
                                DialogBuyOrderConfirmBinding binding = DataBindingUtil.inflate(LayoutInflater.from(Utils.getContext()), R.layout.dialog_buy_order_confirm, null, false);
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
                                final BuyOrderConfirmViewModel buyOrderConfirmViewModel = new BuyOrderConfirmViewModel(getApplication());
                                binding.setViewModel(buyOrderConfirmViewModel);
                                nhOrderBean.minerFee = feeModel.data.amount;
                                nhOrderBean.feeSymbol = "COCOS";
                                buyOrderConfirmViewModel.setBuyOrderModel(nhOrderBean);
                                dialog.show();
                            }
                        });
                    }
                });
            } else if (TextUtils.equals(EventTypeGlobal.DIALOG_DISMISS_TYPE, busCarrier.getEventType())) {
                if (null != dialog) {
                    dialog.dismiss();
                }
            } else if (TextUtils.equals(EventTypeGlobal.SHOW_CANCEL_ORDER_PASSWORD_VERIFY_DIALOG, busCarrier.getEventType())) {
                if (null != dialog) {
                    dialog.dismiss();
                }
                final NhAssetOrderEntity.NhOrderBean nhOrderBean = (NhAssetOrderEntity.NhOrderBean) busCarrier.getObject();
                showCancelOrderPasswordVerifyDialog(nhOrderBean);
            } else if (TextUtils.equals(EventTypeGlobal.SHOW_BUY_ORDER_PASSWORD_VERIFY_DIALOG, busCarrier.getEventType())) {
                if (null != dialog) {
                    dialog.dismiss();
                }
                final NhAssetOrderEntity.NhOrderBean nhOrderBean = (NhAssetOrderEntity.NhOrderBean) busCarrier.getObject();
                showBuyOrderPasswordVerifyDialog(nhOrderBean);
            }
        }
    }

    private void showBuyOrderPasswordVerifyDialog(final NhAssetOrderEntity.NhOrderBean nhOrderBean) {
        final BaseVerifyPasswordDialog passwordVerifyDialog = new BaseVerifyPasswordDialog();
        passwordVerifyDialog.show(getSupportFragmentManager(), "passwordVerifyDialog");
        passwordVerifyDialog.setPasswordListener(new BaseVerifyPasswordDialog.IPasswordListener() {
            @Override
            public void onFinish(String password) {
                CocosBcxApiWrapper.getBcxInstance().buy_nh_asset(password, AccountHelperUtils.getCurrentAccountName(), nhOrderBean.id, new IBcxCallBack() {
                    @Override
                    public void onReceiveValue(String s) {
                        Log.i("buy_nh_asset", s);
                        final OperateResultModel operateResultModel = GsonSingleInstance.getGsonInstance().fromJson(s, OperateResultModel.class);

                        if (operateResultModel.code == 161) {
                            ToastUtils.showShort(R.string.module_asset_order_not_exist);
                            return;
                        }

                        if (operateResultModel.code == 105) {
                            ToastUtils.showShort(R.string.module_asset_wrong_password);
                            return;
                        }

                        if (!operateResultModel.isSuccess()) {
                            ToastUtils.showShort(R.string.net_work_failed);
                            return;
                        }
                        allNhOrderFragment.loadData();
                        ToastUtils.showShort(R.string.module_asset_order_buy_success);
                    }
                });
            }

            @Override
            public void cancel() {

            }
        });
    }

    /**
     * 弹出取消订单密码验证弹窗
     *
     * @param nhOrderBean
     */
    private void showCancelOrderPasswordVerifyDialog(final NhAssetOrderEntity.NhOrderBean nhOrderBean) {
        final BaseVerifyPasswordDialog passwordVerifyDialog = new BaseVerifyPasswordDialog();
        passwordVerifyDialog.show(getSupportFragmentManager(), "passwordVerifyDialog");
        passwordVerifyDialog.setPasswordListener(new BaseVerifyPasswordDialog.IPasswordListener() {
            @Override
            public void onFinish(String password) {
                CocosBcxApiWrapper.getBcxInstance().cancel_nh_asset_order(nhOrderBean.seller, password, nhOrderBean.id, "COCOS", new IBcxCallBack() {
                    @Override
                    public void onReceiveValue(String s) {
                        Log.i("cancel_nh_asset_order", s);
                        final OperateResultModel operateResultModel = GsonSingleInstance.getGsonInstance().fromJson(s, OperateResultModel.class);

                        if (operateResultModel.code == 161) {
                            ToastUtils.showShort(R.string.module_asset_order_not_exist);
                            return;
                        }

                        if (operateResultModel.code == 105) {
                            ToastUtils.showShort(R.string.module_asset_wrong_password);
                            return;
                        }

                        if (!operateResultModel.isSuccess()) {
                            ToastUtils.showShort(R.string.net_work_failed);
                            return;
                        }
                        mineNhOrderFragment.loadData();
                        allNhOrderFragment.loadData();
                        ToastUtils.showShort(R.string.module_asset_order_cancel_success);
                    }
                });
            }

            @Override
            public void cancel() {

            }
        });
    }

    private void initTabListener() {
        binding.tabLayout.setTabData(mTabEntities);
        binding.tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                binding.viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                binding.tabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.viewPager.setCurrentItem(0);
    }
}
