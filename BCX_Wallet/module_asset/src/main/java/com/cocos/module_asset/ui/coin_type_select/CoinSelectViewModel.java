package com.cocos.module_asset.ui.coin_type_select;

import android.app.Application;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.bcx_sdk.bcx_log.LogUtils;
import com.cocos.bcx_sdk.bcx_wallet.chain.asset_object;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.AllAssetBalanceModel;
import com.cocos.library_base.entity.AssetsModel;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_asset.BR;
import com.cocos.module_asset.R;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @author ningkang.guo
 * @Date 2019/4/10
 */
public class CoinSelectViewModel extends BaseViewModel {

    private List<AssetsModel.AssetModel> assetModels = new ArrayList<>();

    public ObservableField<String> coinSelectTitle = new ObservableField<>("");

    public ObservableInt emptyViewVisible = new ObservableInt(View.GONE);

    public ObservableInt recyclerViewVisible = new ObservableInt(View.VISIBLE);

    private String preAccountId;
    private int operateType;

    public CoinSelectViewModel(@NonNull Application application) {
        super(application);
    }

    public ObservableList<CoinSelectItemViewModel> observableList = new ObservableArrayList<>();

    public ItemBinding<CoinSelectItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.module_asset_item_coin_select);

    public final BindingRecyclerViewAdapter<CoinSelectItemViewModel> adapter = new BindingRecyclerViewAdapter<>();

    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    public void setOperateType(int operateType) {
        this.operateType = operateType;
        coinSelectTitle.set(operateType == 1 ? Utils.getString(R.string.module_asset_coin_select_transfer_title) : Utils.getString(R.string.module_asset_coin_select_receivables_title));
    }

    /**
     * 查询账户资产
     */
    public void requestAssetsListData() {
        final String accountId = AccountHelperUtils.getCurrentAccountId();
        LogUtils.d("accountId", accountId);
        // 如果不是同一个账号则清除数据
        if (!TextUtils.equals(preAccountId, accountId)) {
            assetModels.clear();
            observableList.clear();
        }
        preAccountId = accountId;
        if (TextUtils.isEmpty(accountId)) {
            emptyViewVisible.set(View.VISIBLE);
            recyclerViewVisible.set(View.GONE);
            return;
        }
        CocosBcxApiWrapper.getBcxInstance().get_all_account_balances(accountId, new IBcxCallBack() {
            @Override
            public void onReceiveValue(final String s) {
                LogUtils.d("get_account_balances", s);
                MainHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        AllAssetBalanceModel balanceEntity = GsonSingleInstance.getGsonInstance().fromJson(s, AllAssetBalanceModel.class);
                        if (!balanceEntity.isSuccess() || balanceEntity.getData().size() <= 0) {
                            emptyViewVisible.set(View.VISIBLE);
                            recyclerViewVisible.set(View.GONE);
                            return;
                        }
                        final List<AllAssetBalanceModel.DataBean> dataBeans = balanceEntity.getData();
                        for (int i = 0; i < dataBeans.size(); i++) {
                            final AllAssetBalanceModel.DataBean dataBean = dataBeans.get(i);
                            if (TextUtils.equals(dataBean.getAsset_id(), "1.3.1")) {
                                continue;
                            }
                            final int finalI = i;
                            CocosBcxApiWrapper.getBcxInstance().lookup_asset_symbols(dataBean.getAsset_id(), new IBcxCallBack() {
                                @Override
                                public void onReceiveValue(final String s) {
                                    LogUtils.d("lookup_asset_symbols", s);
                                    final AssetsModel assetModel = GsonSingleInstance.getGsonInstance().fromJson(s, AssetsModel.class);
                                    if (!assetModel.isSuccess()) {
                                        return;
                                    }
                                    MainHandler.getInstance().post(new Runnable() {
                                        @Override
                                        public void run() {
                                            AssetsModel.AssetModel assetModel1 = assetModel.getData();
                                            assetModel1.amount = dataBean.getAmount();
                                            assetModel1.operateType = operateType;

                                            if (assetModels.size() == dataBeans.size()) {
                                                if (!assetModel1.equals(assetModels.get(finalI))) {
                                                    assetModels.set(finalI, assetModel1);
                                                    CoinSelectItemViewModel itemViewModel = new CoinSelectItemViewModel(CoinSelectViewModel.this, assetModel1);
                                                    observableList.set(finalI, itemViewModel);
                                                }
                                            } else {
                                                assetModels.add(assetModel1);
                                                CoinSelectItemViewModel itemViewModel = new CoinSelectItemViewModel(CoinSelectViewModel.this, assetModel1);
                                                observableList.add(itemViewModel);
                                            }
                                            emptyViewVisible.set(View.GONE);
                                            recyclerViewVisible.set(View.VISIBLE);
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }
        });
    }


    /**
     * 查询全网资产
     */
    public void requestAllBalances() {
        assetModels.clear();
        observableList.clear();
        List<asset_object> asset_objects = CocosBcxApiWrapper.getBcxInstance().list_assets_sync("A", 100);
        for (asset_object asset_object : asset_objects) {
            AssetsModel.AssetModel assetModel1 = new AssetsModel.AssetModel();
            if (TextUtils.equals(asset_object.symbol, "GAS")) {
                continue;
            }
            assetModel1.symbol = asset_object.symbol;
            assetModel1.operateType = operateType;
            CoinSelectItemViewModel itemViewModel = new CoinSelectItemViewModel(CoinSelectViewModel.this, assetModel1);
            if (TextUtils.equals(assetModel1.symbol, "COCOS")) {
                observableList.add(0, itemViewModel);
            } else {
                observableList.add(itemViewModel);
            }
        }
    }


}
