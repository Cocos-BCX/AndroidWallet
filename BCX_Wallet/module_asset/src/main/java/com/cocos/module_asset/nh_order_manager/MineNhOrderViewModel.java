package com.cocos.module_asset.nh_order_manager;

import android.app.Application;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.view.View;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.module_asset.BR;
import com.cocos.module_asset.R;
import com.cocos.module_asset.entity.NhOrderEntity;

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
        NhOrderEntity nhOrderEntity = new NhOrderEntity();
        MineNhOrderItemViewModel itemViewModel = new MineNhOrderItemViewModel(MineNhOrderViewModel.this, nhOrderEntity);
        observableList.add(itemViewModel);
    }
}
