package com.cocos.module_mine.mine_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cocos.library_base.base.BaseFragment;
import com.cocos.library_base.utils.StatusBarUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.module_mine.BR;
import com.cocos.module_mine.R;
import com.cocos.module_mine.databinding.FragmentMineBinding;

/**
 * Created by guoningkang on 2019/2/12.
 */

public class MineFragment extends BaseFragment<FragmentMineBinding, MineViewModel> {


    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_mine;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        int statusHeight = StatusBarUtils.getStatusBarHeight(Utils.getContext());
        binding.mineTitle.setPadding(0, statusHeight, 0, 0);
    }
}
