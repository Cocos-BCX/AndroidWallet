package com.cocos.module_asset.nh_order_manager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cocos.library_base.base.BaseFragment;
import com.cocos.library_base.utils.Utils;
import com.cocos.module_asset.BR;
import com.cocos.module_asset.R;
import com.cocos.module_asset.databinding.FragmentAllNhOrderBinding;

import in.srain.cube.views.ptr.PtrClassicDefaultFooter;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * @author ningkang.guo
 * @Date 2019/7/15
 */
public class AllNhOrderFragment extends BaseFragment<FragmentAllNhOrderBinding, AllNhOrderViewModel> {

    private int page = 1;
    private int pageSize = 6;

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
        MaterialHeader materialHeader = new MaterialHeader(getActivity());
        materialHeader.setColorSchemeColors(new int[]{Utils.getColor(R.color.color_262A33), Utils.getColor(R.color.color_4868DC), Utils.getColor(R.color.color_A5A9B1)});
        binding.ptrFrameLayout.setHeaderView(materialHeader);

        PtrClassicDefaultFooter ptrClassicDefaultFooter = new PtrClassicDefaultFooter(getActivity());
        binding.ptrFrameLayout.setFooterView(ptrClassicDefaultFooter);

        binding.ptrFrameLayout.addPtrUIHandler(materialHeader);
        binding.ptrFrameLayout.addPtrUIHandler(ptrClassicDefaultFooter);

        binding.ptrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                page++;
                loadData();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                loadData();
            }
        });
        binding.ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
        loadData();
    }

    public void loadData() {
        viewModel.requestAssetsListData(page, pageSize, binding.ptrFrameLayout);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }


}
