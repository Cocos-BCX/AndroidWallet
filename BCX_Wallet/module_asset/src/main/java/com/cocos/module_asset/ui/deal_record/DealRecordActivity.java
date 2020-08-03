package com.cocos.module_asset.ui.deal_record;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.bcx_sdk.bcx_log.LogUtils;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.entity.AssetsModel;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_asset.BR;
import com.cocos.module_asset.R;
import com.cocos.module_asset.databinding.ActivityDealRecordBinding;
import com.cocos.module_asset.databinding.ActivityDealRecordNewBinding;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * @author ningkang.guo
 * @Date 2019/2/20
 */
@Route(path = RouterActivityPath.ACTIVITY_DEAL_RECORD)
public class DealRecordActivity extends BaseActivity<ActivityDealRecordNewBinding, DealRecordViewModel> {

    private AssetsModel.AssetModel assetModel;
    private boolean isFirst = true;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_deal_record_new;
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
        viewModel.setLoadListener(new DealRecordViewModel.LoadListener() {
            @Override
            public void onFinishRefresh(boolean isAllData) {
                binding.smartRefreshLayout.finishRefresh(true);
//                binding.smartRefreshLayout.setEnableLoadMore(!isAllData);
            }

            @Override
            public void onFinishLoad(boolean isAllData) {
                binding.smartRefreshLayout.finishLoadMore();
//                if (isAllData) {
//                    binding.smartRefreshLayout.finishLoadMoreWithNoMoreData();
//                }
            }
        });
        viewModel.requestDealRecordList(DealRecordViewModel.LoadType.REFRESH);
        binding.smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.requestDealRecordList(DealRecordViewModel.LoadType.REFRESH);
            }
        });
        binding.smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LogUtils.d("加载==","上拉");
                viewModel.requestDealRecordList(DealRecordViewModel.LoadType.ONLOAD);
            }
        });

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
