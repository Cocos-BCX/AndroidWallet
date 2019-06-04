package com.cocos.module_mine.asset_overview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cocos.library_base.base.BaseFragment;
import com.cocos.module_mine.BR;
import com.cocos.module_mine.R;
import com.cocos.module_mine.databinding.FragmentNumberAssetBinding;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by guoningkang on 2019/2/12
 */

public class NumberAssetFragment extends BaseFragment<FragmentNumberAssetBinding, NumberAssetViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_number_asset;
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
