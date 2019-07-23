package com.cocos.module_mine.asset_overview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cocos.library_base.base.BaseFragment;
import com.cocos.library_base.utils.Utils;
import com.cocos.module_mine.BR;
import com.cocos.module_mine.R;
import com.cocos.module_mine.databinding.FragmentPropAssetBinding;

import in.srain.cube.views.ptr.PtrClassicDefaultFooter;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;


/**
 * Created by guoningkang on 2019/2/12
 */
public class NHAssetFragment extends BaseFragment<FragmentPropAssetBinding, NHAssetViewModel> {

    int page = 1;

    int pageSize = 8;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_prop_asset;
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
        viewModel.requestPropAssetsListData(page, pageSize, binding.ptrFrameLayout);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
}
