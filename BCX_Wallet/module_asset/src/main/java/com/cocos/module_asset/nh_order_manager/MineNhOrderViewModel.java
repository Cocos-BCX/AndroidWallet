package com.cocos.module_asset.nh_order_manager;

import android.annotation.SuppressLint;
import android.app.Application;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.bcx_sdk.bcx_wallet.chain.asset_object;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.entity.NhAssetOrderEntity;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_asset.BR;
import com.cocos.module_asset.R;

import java.util.List;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @author ningkang.guo
 * @Date 2019/7/15
 */
public class MineNhOrderViewModel extends BaseViewModel {

    public MineNhOrderViewModel(@NonNull Application application) {
        super(application);
    }

    public ObservableInt emptyViewVisible = new ObservableInt(View.GONE);

    public ObservableInt recyclerViewVisible = new ObservableInt(View.VISIBLE);

    public ObservableList<MineNhOrderItemViewModel> observableList = new ObservableArrayList<>();

    public ItemBinding<MineNhOrderItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.module_asset_item_mine_nh_order);

    public final BindingRecyclerViewAdapter<MineNhOrderItemViewModel> adapter = new BindingRecyclerViewAdapter<>();

    /**
     * 加载我的NH订单
     */
    public void requestAssetsListData() {

        CocosBcxApiWrapper.getBcxInstance().list_account_nh_asset_order(AccountHelperUtils.getCurrentAccountName(), 10, 1, new IBcxCallBack() {
            @SuppressLint("LongLogTag")
            @Override
            public void onReceiveValue(String s) {
                Log.i("list_account_nh_asset_order", s);
                final NhAssetOrderEntity nhOrderEntity = GsonSingleInstance.getGsonInstance().fromJson(s, NhAssetOrderEntity.class);
                MainHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        if (!nhOrderEntity.isSuccess() || nhOrderEntity.getData() == null || nhOrderEntity.getData().size() <= 0) {
                            emptyViewVisible.set(View.VISIBLE);
                            recyclerViewVisible.set(View.GONE);
                            return;
                        }
                        List<NhAssetOrderEntity.NhOrderBean> nhOrderBeans = nhOrderEntity.getData();
                        for (NhAssetOrderEntity.NhOrderBean nhOrderBean : nhOrderBeans) {
                            asset_object asset_object = CocosBcxApiWrapper.getBcxInstance().get_asset_object(nhOrderBean.price.asset_id);
                            nhOrderBean.priceWithSymbol = nhOrderBean.price.amount / (Math.pow(10, asset_object.precision)) + asset_object.symbol;
                            nhOrderBean.sellerName = AccountHelperUtils.getCurrentAccountName();
                            MineNhOrderItemViewModel itemViewModel = new MineNhOrderItemViewModel(MineNhOrderViewModel.this, nhOrderBean);
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
