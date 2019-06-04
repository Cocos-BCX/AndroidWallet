package com.cocos.module_mine.asset_overview;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.base.ItemViewModel;
import com.cocos.module_mine.BR;
import com.cocos.module_mine.R;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;


public class ViewPagerItemViewModel extends ItemViewModel {


    public ViewPagerItemViewModel(@NonNull BaseViewModel viewModel) {
        super(viewModel);
    }

    //当前帐户总资产
    public ObservableField<String> totalAsset = new ObservableField<>();

    public ObservableList<RecyclerAssetItemViewModel> observableList = new ObservableArrayList<>();

    public ItemBinding<RecyclerAssetItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.module_mine_number_assets_overview_item);

    public final BindingRecyclerViewAdapter<RecyclerAssetItemViewModel> adapter = new BindingRecyclerViewAdapter<>();

   /* public void requestAssetsListData(String accountName, String unit) {
        BcxSDkInstance.getBcxInstance().queryAccountAllBalances(accountName, unit, new IBcxCallBack() {
            @Override
            public void onReceiveValue(String s) {
                AssetsModel assetsEntity = GsonSingleInstance.getGsonInstance().fromJson(s, AssetsModel.class);
                if (!assetsEntity.isSuccess()) {
                    return;
                }
                if (null != assetsEntity.getData() && assetsEntity.getData().size() > 0) {
                    BigDecimal totalAssets = BigDecimal.ZERO;
                    observableList.clear();
                    for (AssetsModel.AssetModel assetModel : assetsEntity.getData()) {
                        RecyclerAssetItemViewModel itemViewModel = new RecyclerAssetItemViewModel(ViewPagerItemViewModel.this, assetModel);
                        observableList.add(itemViewModel);
                        totalAssets = totalAssets.add(new BigDecimal(assetModel.getBalance()).multiply(assetModel.getEqValue()).setScale(2, RoundingMode.HALF_UP));
                    }
                    totalAsset.set(String.valueOf(totalAssets));
                } else {
                    //code错误时也可以定义Observable回调到View层去处理
                    ToastUtils.showShort(R.string.module_mine_response_error);
                }
            }
        });

    }*/

}
