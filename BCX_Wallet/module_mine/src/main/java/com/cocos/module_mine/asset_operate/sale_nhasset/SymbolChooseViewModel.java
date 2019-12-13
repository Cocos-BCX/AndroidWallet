package com.cocos.module_mine.asset_operate.sale_nhasset;

import android.app.Application;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_error.NetworkStatusException;
import com.cocos.bcx_sdk.bcx_wallet.chain.asset_object;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.module_mine.BR;
import com.cocos.module_mine.R;

import java.util.List;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @author ningkang.guo
 */
public class SymbolChooseViewModel extends BaseViewModel {


    public SymbolChooseViewModel(@NonNull Application application) {
        super(application);
    }

    public ObservableList<SymbolChooseItemViewModel> observableList = new ObservableArrayList<>();

    public ItemBinding<SymbolChooseItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.module_mine_item_coin_select);

    public final BindingRecyclerViewAdapter<SymbolChooseItemViewModel> adapter = new BindingRecyclerViewAdapter<>();

    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    public void requestAllSymbols() {
        List<asset_object> asset_objects = null;
        try {
            asset_objects = CocosBcxApiWrapper.getBcxInstance().list_assets_sync("A", 100);
            if (null == asset_objects || asset_objects.size() <= 0) {
                return;
            }
            observableList.clear();
            for (asset_object asset_object : asset_objects) {
                SymbolChooseItemViewModel itemViewModel = new SymbolChooseItemViewModel(SymbolChooseViewModel.this, asset_object);
                if (TextUtils.equals("COCOS", asset_object.symbol)) {
                    observableList.add(0, itemViewModel);
                    continue;
                }
                observableList.add(itemViewModel);
            }
        } catch (NetworkStatusException e) {
            ToastUtils.showShort(R.string.net_work_failed);
        }
    }
}
