package com.cocos.module_asset.ui.asset;

import android.app.Application;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
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
        int statusHeight = StatusBarUtils.getStatusBarHeight(getActivity());
        binding.assetTitle.setPadding(0, statusHeight, DensityUtils.dip2px(getActivity(), 20), 0);
        refreshAssetData();
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
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshAssetData();
    }

    @Override
    protected void initHandlerEvent(EventBusCarrier busCarrier) {
        if (TextUtils.equals(EventTypeGlobal.DIALOG_DISMISS_TYPE, busCarrier.getEventType())) {
            dialog.dismiss();
        } else if (TextUtils.equals(EventTypeGlobal.SWITCH_ACCOUNT, busCarrier.getEventType())) {
            refreshAssetData();
        }
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.accountItemObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                dialog = new BottomSheetDialog(getActivity());
                DialogSwitchAccountBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.dialog_switch_account, null, false);
                dialog.setContentView(binding.getRoot());
                binding.setViewModel(new SwitchAccountViewModel((Application) Utils.getContext()));
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });
    }


}
