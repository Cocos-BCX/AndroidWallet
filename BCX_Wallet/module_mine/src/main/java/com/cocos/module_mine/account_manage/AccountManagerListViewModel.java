package com.cocos.module_mine.account_manage;

import android.app.Application;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.view.View;

import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_entity.AccountEntity;
import com.cocos.bcx_sdk.bcx_error.AccountNotFoundException;
import com.cocos.bcx_sdk.bcx_error.NetworkStatusException;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.AssetBalanceModel;
import com.cocos.library_base.entity.AssetsModel;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
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

    public ObservableInt LoginViewVisible = new ObservableInt(View.INVISIBLE);

    public ObservableInt recycleViewVisible = new ObservableInt(View.VISIBLE);

    public ObservableField<String> loginText = new ObservableField<>(Utils.getString(R.string.module_asset_fragment_asset_login_text));

    //条目的点击事件
    public BindingCommand backOnClickCommand = new BindingCommand(() -> finish());

    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public ObservableBoolean loginViewObservable = new ObservableBoolean(false);
    }


    //登陆注册按钮的点击事件
    public BindingCommand loginViewClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.loginViewObservable.set(!uc.loginViewObservable.get());
        }
    });

    /**
     * @param accounts
     */
    public void requestAccountsListData(List<AccountEntity.AccountBean> accounts) {
        if (null == accounts || accounts.size() <= 0) {
            LoginViewVisible.set(View.VISIBLE);
            recycleViewVisible.set(View.INVISIBLE);
            return;
        }
        LoginViewVisible.set(View.INVISIBLE);
        recycleViewVisible.set(View.VISIBLE);
        observableList.clear();
        for (final AccountEntity.AccountBean daoAccount : accounts) {
            String accountId = null;
            try {
                accountId = CocosBcxApiWrapper.getBcxInstance().get_account_id_by_name_sync(daoAccount.getName());
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
                            AccountManagerListItemViewModel itemViewModel = new AccountManagerListItemViewModel(AccountManagerListViewModel.this, assetModel1, daoAccount);
                            observableList.add(itemViewModel);
                        });
                    });
                });
            } catch (NetworkStatusException e) {
                ToastUtils.showShort(com.cocos.library_base.R.string.net_work_failed);
            } catch (AccountNotFoundException e) {
                ToastUtils.showShort(com.cocos.library_base.R.string.account_not_found);
            } catch (Exception e) {
                ToastUtils.showShort(com.cocos.library_base.R.string.net_work_failed);
            }
        }
    }
}
