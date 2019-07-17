package com.cocos.module_mine.asset_overview;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.module_mine.BR;
import com.cocos.module_mine.R;
import com.cocos.module_mine.databinding.ActivityNhAssetDetaiilBinding;
import com.cocos.module_mine.entity.NHAssetModel;

import java.util.Objects;

/**
 * @author ningkang.guo
 * @Date 2019/7/16
 */
@Route(path = RouterActivityPath.ACTIVITY_NH_ASSET_DETAIL)
public class NHAssetDetailActivity extends BaseActivity<ActivityNhAssetDetaiilBinding, NHAssetDetailViewModel> {

    NHAssetModel.NHAssetModelBean NHAssetModelBean;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_nh_asset_detaiil;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        try {
            NHAssetModelBean = (NHAssetModel.NHAssetModelBean) Objects.requireNonNull(getIntent().getExtras()).getSerializable(IntentKeyGlobal.NH_ASSET_MODEL);
        } catch (Exception e) {
        }
    }

    @Override
    public void initData() {
        viewModel.requestAssetDetailData(NHAssetModelBean);
    }
}
