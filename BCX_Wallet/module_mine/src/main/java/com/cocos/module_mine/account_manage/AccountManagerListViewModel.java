package com.cocos.module_mine.account_manage;

import android.app.Application;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;

import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.AssetBalanceModel;
import com.cocos.library_base.entity.AssetsModel;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_mine.BR;
import com.cocos.module_mine.R;

import java.util.List;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @author ningkang.guo
 * @Date 2019/4/8
 */
public class AccountManagerListViewModel extends BaseViewModel {

    public AccountManagerListViewModel(@NonNull Application application) {
        super(application);
    }

    public ObservableList<AccountManagerListItemViewModel> observableList = new ObservableArrayList<>();

    public ItemBinding<AccountManagerListItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.module_mine_item_account_manage);

    public final BindingRecyclerViewAdapter<AccountManagerListItemViewModel> adapter = new BindingRecyclerViewAdapter<>();


    //条目的点击事件
    public BindingCommand backOnClickCommand = new BindingCommand(() -> finish());

    /**
     * @param accountNames
     */
    public void requestAccountsListData(List<String> accountNames) {
        if (null == accountNames || accountNames.size() <= 0) {
            return;
        }
        observableList.clear();
        for (final String accountName : accountNames) {
            String accountId = CocosBcxApiWrapper.getBcxInstance().get_account_id_by_name(accountName);
            CocosBcxApiWrapper.getBcxInstance().get_account_balances(accountId, "1.3.0", s -> {
                AssetBalanceModel balanceEntity = GsonSingleInstance.getGsonInstance().fromJson(s, AssetBalanceModel.class);
                if (null == balanceEntity || !balanceEntity.isSuccess()) {
                    return;
                }
                final AssetBalanceModel.DataBean dataBean = balanceEntity.data;
                CocosBcxApiWrapper.getBcxInstance().lookup_asset_symbols(dataBean.asset_id, s1 -> {
                    final AssetsModel assetModel = GsonSingleInstance.getGsonInstance().fromJson(s1, AssetsModel.class);
                    if (!assetModel.isSuccess()) {
                        return;
                    }
                    MainHandler.getInstance().post(() -> {
                        AssetsModel.AssetModel assetModel1 = assetModel.getData();
                        assetModel1.amount = dataBean.amount;
                        AccountManagerListItemViewModel itemViewModel = new AccountManagerListItemViewModel(AccountManagerListViewModel.this, assetModel1, accountName);
                        observableList.add(itemViewModel);
                    });
                });

            });
        }
    }
}
