package com.cocos.module_asset.ui.deal_record;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.entity.AssetsModel;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_asset.BR;
import com.cocos.module_asset.R;
import com.cocos.module_asset.databinding.ActivityDealRecordBinding;

/**
 * @author ningkang.guo
 * @Date 2019/2/20
 */
@Route(path = RouterActivityPath.ACTIVITY_DEAL_RECORD)
public class DealRecordActivity extends BaseActivity<ActivityDealRecordBinding, DealRecordViewModel> {

    private AssetsModel.AssetModel assetModel;
    private boolean isFirst = true;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_deal_record;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }


    @Override
    public void initParam() {
        try {
            Bundle bundle = getIntent().getExtras();
            assetModel = (AssetsModel.AssetModel) bundle.getSerializable(IntentKeyGlobal.ASSET_MODEL);
        } catch (Exception e) {
        }
    }

    @Override
    public void initData() {
        viewModel.setAssetModel(assetModel);
        viewModel.requestDealRecordList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirst) {
            isFirst = false;
            return;
        }
//        viewModel.requestDealRecordList();
        viewModel.requestAssetsListData();
    }
}
