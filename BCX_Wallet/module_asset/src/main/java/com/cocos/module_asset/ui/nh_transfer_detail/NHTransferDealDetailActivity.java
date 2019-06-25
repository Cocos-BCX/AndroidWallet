package com.cocos.module_asset.ui.nh_transfer_detail;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.module_asset.BR;
import com.cocos.module_asset.R;
import com.cocos.module_asset.databinding.ActivityNhTransferDealDetailBinding;
import com.cocos.module_asset.entity.DealDetailModel;

/**
 * @author ningkang.guo
 * @Date 2019/2/20
 */
@Route(path = RouterActivityPath.ACTIVITY_NH_TRANSFER_RECORD_DETAIL)
public class NHTransferDealDetailActivity extends BaseActivity<ActivityNhTransferDealDetailBinding, NHTransferDealDetailViewModel> {

    private DealDetailModel dealDetailModel;

    @Override
    public int initContentView(Bundle bundle) {
        return R.layout.activity_nh_transfer_deal_detail;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        try {
            dealDetailModel = (DealDetailModel) getIntent().getExtras().getSerializable(IntentKeyGlobal.DEAL_DETAIL_MODEL);
        } catch (Exception e) {

        }
    }

    @Override
    public void initData() {
        viewModel.setDealDetailData(dealDetailModel);
    }

}
