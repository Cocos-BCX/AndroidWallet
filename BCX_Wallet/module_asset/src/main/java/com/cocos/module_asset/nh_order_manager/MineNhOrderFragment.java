package com.cocos.module_asset.nh_order_manager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cocos.library_base.base.BaseFragment;
import com.cocos.module_asset.BR;
import com.cocos.module_asset.R;
import com.cocos.module_asset.databinding.FragmentMineNhOrderBinding;

/**
 * @author ningkang.guo
 * @Date 2019/7/15
 */
public class MineNhOrderFragment extends BaseFragment<FragmentMineNhOrderBinding, MineNhOrderViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_mine_nh_order;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        loadData();
    }

    public void loadData() {
        viewModel.requestAssetsListData();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
}
