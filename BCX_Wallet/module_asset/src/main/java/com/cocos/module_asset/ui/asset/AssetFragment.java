package com.cocos.module_asset.ui.asset;

import android.app.Activity;
import android.app.Application;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.library_base.base.BaseFragment;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.component.switch_account.SwitchAccountViewModel;
import com.cocos.library_base.databinding.DialogSwitchAccountBinding;
import com.cocos.library_base.entity.AccountNamesEntity;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.DensityUtils;
import com.cocos.library_base.utils.HttpUtils;
import com.cocos.library_base.utils.StatusBarUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_asset.BR;
import com.cocos.module_asset.R;
import com.cocos.module_asset.databinding.FragmentAssetBinding;

import java.util.Arrays;
import java.util.List;


/**
 * Created by guoningkang on 2019/2/12
 */

public class AssetFragment extends BaseFragment<FragmentAssetBinding, AssetViewModel> {

    private BottomSheetDialog dialog;
    private boolean isFirst = true;
    private boolean isInit = false;
    private Activity activity;
    private long tryCount;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_asset;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        if (null == activity) {
            this.activity = getActivity();
        }
        int statusHeight = StatusBarUtils.getStatusBarHeight(Utils.getContext());
        binding.assetTitle.setPadding(0, statusHeight, DensityUtils.dip2px(Utils.getContext(), 20), 0);
        initAccountData(500);
        isInit = true;
        Log.i("refreshAssetData", "initData");
    }

    public void initAccountData(long delayTime) {
        Log.i("initAccountData:", String.valueOf(delayTime));
        MainHandler.getInstance().postDelayed(new Runnable() {
            @Override
            public void run() {
                CocosBcxApiWrapper.getBcxInstance().queryAccountNamesByChainId(new IBcxCallBack() {
                    @Override
                    public void onReceiveValue(String s) {
                        Log.i("refreshAssetData:", s);
                        AccountNamesEntity accountNamesEntity = GsonSingleInstance.getGsonInstance().fromJson(s, AccountNamesEntity.class);
                        if (accountNamesEntity.isSuccess()) {
                            List<String> accountNames = Arrays.asList(accountNamesEntity.data.split(","));
                            if (!accountNames.contains(AccountHelperUtils.getCurrentAccountName())) {
                                AccountHelperUtils.setCurrentAccountName(accountNames.get(0));
                            }
                            viewModel.setAccountName();
                            viewModel.requestAssetsListData();
                            isFirst = false;
                        } else if (accountNamesEntity.code == 0) {
                            //todo 显示创建和登录按钮
                            viewModel.emptyViewVisible.set(View.GONE);
                            viewModel.recyclerViewVisible.set(View.GONE);
                            viewModel.LoginViewVisible.set(View.VISIBLE);
                            viewModel.accountViewVisible.set(View.INVISIBLE);
                            AccountHelperUtils.setCurrentAccountName("");
                            viewModel.setAccountName();
                            isFirst = false;
                        } else if (accountNamesEntity.code == 177) {
                            if (tryCount > 8) {
                                viewModel.emptyViewVisible.set(View.VISIBLE);
                                viewModel.recyclerViewVisible.set(View.GONE);
                                viewModel.LoginViewVisible.set(View.GONE);
                                viewModel.accountViewVisible.set(View.GONE);
                                viewModel.setAccountName();
                                isFirst = false;
                                return;
                            }
                            initAccountData(100);
                            tryCount++;
                            isFirst = false;
                        }
                    }
                });
            }
        }, delayTime);
    }

    /**
     * 刷新资产页数据
     */
    private void refreshAssetData() {
        try {
            CocosBcxApiWrapper.getBcxInstance().queryAccountNamesByChainId(new IBcxCallBack() {
                @Override
                public void onReceiveValue(String s) {
                    Log.i("refreshAssetData:", s);
                    AccountNamesEntity accountNamesEntity = GsonSingleInstance.getGsonInstance().fromJson(s, AccountNamesEntity.class);
                    if (accountNamesEntity.isSuccess()) {
                        List<String> accountNames = Arrays.asList(accountNamesEntity.data.split(","));
                        if (!accountNames.contains(AccountHelperUtils.getCurrentAccountName())) {
                            AccountHelperUtils.setCurrentAccountName(accountNames.get(0));
                        }
                        viewModel.setAccountName();
                        viewModel.requestAssetsListData();
                        isFirst = false;
                    } else {
                        //todo 显示创建和登录按钮
                        viewModel.emptyViewVisible.set(View.GONE);
                        viewModel.recyclerViewVisible.set(View.GONE);
                        viewModel.LoginViewVisible.set(View.VISIBLE);
                        viewModel.accountViewVisible.set(View.INVISIBLE);
                        AccountHelperUtils.setCurrentAccountName("");
                        viewModel.setAccountName();
                        isFirst = false;
                    }
                }
            });
            HttpUtils.getCocosPrice();
        } catch (Exception e) {
            //    refreshAssetData();
            Log.i("refreshAssetData:", e.getMessage());
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isFirst) {
            refreshAssetData();
            Log.i("refreshAssetData", "setUserVisibleHint");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isInit) {
            refreshAssetData();
            Log.i("refreshAssetData", "onResume");
        }
        isInit = false;

    }

    @Override
    protected void initHandlerEvent(EventBusCarrier busCarrier) {
        if (TextUtils.equals(EventTypeGlobal.DIALOG_DISMISS_TYPE, busCarrier.getEventType())) {
            dialog.dismiss();
        } else if (TextUtils.equals(EventTypeGlobal.SWITCH_ACCOUNT, busCarrier.getEventType())) {
            viewModel.setAccountName();
            viewModel.requestAssetsListData();
            Log.i("refreshAssetData", "SWITCH_ACCOUNT");
        }
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.accountItemObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                dialog = new BottomSheetDialog(activity);
                DialogSwitchAccountBinding binding = DataBindingUtil.inflate(LayoutInflater.from(Utils.getContext()), R.layout.dialog_switch_account, null, false);
                dialog.setContentView(binding.getRoot());
                binding.setViewModel(new SwitchAccountViewModel((Application) Utils.getContext()));
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });
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
