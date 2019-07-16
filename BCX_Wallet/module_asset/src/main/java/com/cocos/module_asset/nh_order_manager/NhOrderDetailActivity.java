package com.cocos.module_asset.nh_order_manager;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.library_base.BR;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.entity.NhAssetOrderEntity;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.module_asset.R;
import com.cocos.module_asset.databinding.ActivityNhOrderDetailBinding;

/**
 * @author ningkang.guo
 * @Date 2019/7/16
 */
@Route(path = RouterActivityPath.ACTIVITY_NH_ORDER_DETAIL)
public class NhOrderDetailActivity extends BaseActivity<ActivityNhOrderDetailBinding, NhOrderDetailViewModel> {

    NhAssetOrderEntity.NhOrderBean nhOrderBean;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_nh_order_detail;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        try {
            nhOrderBean = (NhAssetOrderEntity.NhOrderBean) getIntent().getExtras().getSerializable(IntentKeyGlobal.NH_ORDER_MODEL);
        } catch (Exception e) {
        }
    }

    @Override
    public void initData() {
        viewModel.requestOrderDetailData(nhOrderBean);
    }
}
