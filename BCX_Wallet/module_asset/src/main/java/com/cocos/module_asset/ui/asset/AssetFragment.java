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
import android.view.ViewGroup;

import com.cocos.library_base.base.BaseFragment;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.utils.DensityUtils;
import com.cocos.library_base.utils.StatusBarUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.module_asset.BR;
import com.cocos.module_asset.R;
import com.cocos.module_asset.databinding.DialogSwitchAccountBinding;
import com.cocos.module_asset.databinding.FragmentAssetBinding;
import com.cocos.module_asset.switch_account.SwitchAccountViewModel;


/**
 * Created by guoningkang on 2019/2/12
 */

public class AssetFragment extends BaseFragment<FragmentAssetBinding, AssetViewModel> {

    private BottomSheetDialog dialog;
    private boolean isFirst = true;
    private boolean isInit = false;
    private Activity activity;

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
        refreshAssetData();
        isInit = true;
        Log.i("refreshAssetData", "initData");
    }

    /**
     * 刷新资产页数据
     */
    private void refreshAssetData() {
        try {
            viewModel.setAccountName();
            viewModel.requestAssetsListData();
            isFirst = false;
        } catch (Exception e) {

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
        }
        isInit = false;
        Log.i("refreshAssetData", "onResume");
    }

    @Override
    protected void initHandlerEvent(EventBusCarrier busCarrier) {
        if (TextUtils.equals(EventTypeGlobal.DIALOG_DISMISS_TYPE, busCarrier.getEventType())) {
            dialog.dismiss();
        } else if (TextUtils.equals(EventTypeGlobal.SWITCH_ACCOUNT, busCarrier.getEventType())) {
            refreshAssetData();
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
