package com.cocos.module_asset.nh_order_manager;

import android.os.Bundle;

import com.cocos.library_base.BR;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.module_asset.R;
import com.cocos.module_asset.databinding.ActivityNhOrderDetailBinding;

/**
 * @author ningkang.guo
 * @Date 2019/7/16
 */
public class NhOrderDetailActivity extends BaseActivity<ActivityNhOrderDetailBinding, NhOrderDetailViewModel> {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_nh_order_detail;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        viewModel.requestOrderDetailData();
    }
}
