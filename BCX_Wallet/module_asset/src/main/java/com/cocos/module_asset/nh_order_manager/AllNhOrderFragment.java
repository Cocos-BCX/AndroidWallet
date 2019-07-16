package com.cocos.module_asset.nh_order_manager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cocos.library_base.base.BaseFragment;
import com.cocos.module_asset.BR;
import com.cocos.module_asset.R;
import com.cocos.module_asset.databinding.FragmentAllNhOrderBinding;

/**
 * @author ningkang.guo
 * @Date 2019/7/15
 */
public class AllNhOrderFragment extends BaseFragment<FragmentAllNhOrderBinding,AllNhOrderViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_all_nh_order;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        viewModel.requestAssetsListData();
    }
}
