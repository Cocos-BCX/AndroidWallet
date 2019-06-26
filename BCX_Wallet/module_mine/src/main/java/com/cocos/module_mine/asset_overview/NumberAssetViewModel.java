package com.cocos.module_mine.asset_overview;

import android.app.Application;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.view.View;

import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.bcx_sdk.bcx_log.LogUtils;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.entity.AllAssetBalanceModel;
import com.cocos.library_base.entity.AssetsModel;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_mine.BR;
import com.cocos.module_mine.R;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @author ningkang.guo
 * @Date 2019/2/25
 */
public class NumberAssetViewModel extends BaseViewModel {

    public NumberAssetViewModel(@NonNull Application application) {
        super(application);
    }

    public ObservableInt emptyViewVisible = new ObservableInt(View.VISIBLE);

    public ObservableInt recyclerViewVisible = new ObservableInt(View.GONE);

    public ObservableList<NumberAssetItemViewModel> observableList = new ObservableArrayList<>();

    public ItemBinding<NumberAssetItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.module_mine_item_number_assets);

    public final BindingRecyclerViewAdapter<NumberAssetItemViewModel> adapter = new BindingRecyclerViewAdapter<>();

    /**
     * 数字资产数据加载
     */
    public void requestAssetsListData() {
        String accountId = AccountHelperUtils.getCurrentAccountId();
        observableList.clear();
        CocosBcxApiWrapper.getBcxInstance().get_all_account_balances(accountId, new IBcxCallBack() {
            @Override
            public void onReceiveValue(final String s) {
                LogUtils.d("get_account_balances", s);
                AllAssetBalanceModel balanceEntity = GsonSingleInstance.getGsonInstance().fromJson(s, AllAssetBalanceModel.class);
                if (!balanceEntity.isSuccess() || balanceEntity.getData().size() <= 0) {
                    emptyViewVisible.set(View.VISIBLE);
                    recyclerViewVisible.set(View.GONE);
                    return;
                }
                for (final AllAssetBalanceModel.DataBean dataBean : balanceEntity.getData()) {
                    //todo 价值计算
                    CocosBcxApiWrapper.getBcxInstance().lookup_asset_symbols(dataBean.getAsset_id(), new IBcxCallBack() {
                        @Override
                        public void onReceiveValue(final String s) {
                            LogUtils.d("lookup_asset_symbols", s);
                            MainHandler.getInstance().post(new Runnable() {
                                @Override
                                public void run() {
                                    AssetsModel assetModel = GsonSingleInstance.getGsonInstance().fromJson(s, AssetsModel.class);
                                    if (assetModel.isSuccess()) {
                                        assetModel.getData().amount = dataBean.getAmount();
                                        NumberAssetItemViewModel itemViewModel = new NumberAssetItemViewModel(NumberAssetViewModel.this, assetModel.getData());
                                        observableList.add(itemViewModel);
                                        emptyViewVisible.set(View.GONE);
                                        recyclerViewVisible.set(View.VISIBLE);
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}
