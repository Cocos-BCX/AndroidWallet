package com.cocos.module_asset.ui.coin_type_select;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.module_asset.BR;
import com.cocos.module_asset.R;
import com.cocos.module_asset.databinding.ActivitySelectCoinBinding;

/**
 * @author ningkang.guo
 * @Date 2019/4/10
 */
@Route(path = RouterActivityPath.ACTIVITY_COIN_SELECT)
public class CoinSelectActivity extends BaseActivity<ActivitySelectCoinBinding, CoinSelectViewModel> {

    private int operateType;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_select_coin;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        try {
            operateType = (int) getIntent().getExtras().get(IntentKeyGlobal.OPERATE_TYPE);
        } catch (Exception e) {
        }
    }

    @Override
    public void initData() {
        viewModel.setOperateType(operateType);
        viewModel.requestAssetsListData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.requestAssetsListData();
    }
}
