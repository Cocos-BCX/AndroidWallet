package com.cocos.module_asset.ui.asset;

import android.app.Application;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.bcx_sdk.bcx_log.LogUtils;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.AllAssetBalanceModel;
import com.cocos.library_base.entity.AssetsModel;
import com.cocos.library_base.entity.WebViewModel;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_asset.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.BR;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @author ningkang.guo
 * @Date 2019/2/12
 */
public class AssetViewModel extends BaseViewModel {

    private List<AssetsModel.AssetModel> assetModels = new ArrayList<>();

    public ObservableInt emptyViewVisible = new ObservableInt(View.GONE);

    public ObservableInt recyclerViewVisible = new ObservableInt(View.VISIBLE);

    public String preAccountId;

    public AssetViewModel(@NonNull Application application) {
        super(application);
    }

    public String accountName;


    public void setAccountName() {
        this.accountName = AccountHelperUtils.getCurrentAccountName();
        currentAccountName.set(accountName);
    }

    //当前帐户名户名的绑定
    public ObservableField<String> currentAccountName = new ObservableField<>();

    //当前资产显示的货币单位
    public ObservableField<String> totalAssetCurrencyUnit = new ObservableField<>(Utils.getString(R.string.module_asset_total_assets));

    //当前帐户总资产
    public ObservableField<String> totalAsset = new ObservableField<>();


    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();


    public class UIChangeObservable {
        public ObservableBoolean accountItemObservable = new ObservableBoolean(false);
    }


    //转账按钮的点击事件
    public BindingCommand accountItemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.accountItemObservable.set(!uc.accountItemObservable.get());
        }
    });


    //转账按钮的点击事件
    public BindingCommand transferItemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle = new Bundle();
            bundle.putInt(IntentKeyGlobal.OPERATE_TYPE, 1);
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_COIN_SELECT).with(bundle).navigation();
        }
    });

    //收款按钮的点击事件
    public BindingCommand receivablesItemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle = new Bundle();
            bundle.putInt(IntentKeyGlobal.OPERATE_TYPE, 2);
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_COIN_SELECT).with(bundle).navigation();
        }
    });

    //资源按钮的点击事件
    public BindingCommand propsAssetsItemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            WebViewModel webViewModel = new WebViewModel();
            webViewModel.setTitle(Utils.getString(R.string.module_asset_props_assets));
            webViewModel.setUrl("http://192.168.15.39:8081");
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKeyGlobal.WEB_MODEL, webViewModel);
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_JS_WEB).with(bundle).navigation();
        }
    });

    //投票按钮的点击事件
    public BindingCommand OrderManageCentersItemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            WebViewModel webViewModel = new WebViewModel();
            webViewModel.setTitle(Utils.getString(R.string.module_asset_message_center));
            webViewModel.setUrl("http://192.168.15.39:8080");
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKeyGlobal.WEB_MODEL, webViewModel);
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_JS_WEB).with(bundle).navigation();
        }
    });


    public ObservableList<AssetItemViewModel> observableList = new ObservableArrayList<>();
    public ItemBinding<AssetItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.module_asset_item_assets);
    public final BindingRecyclerViewAdapter<AssetItemViewModel> adapter = new BindingRecyclerViewAdapter<>();

    public void requestAssetsListData() {
        final String accountId = AccountHelperUtils.getCurrentAccountId();
        // 如果不是同一个账号则清除数据
        if (!TextUtils.equals(preAccountId, accountId)) {
            assetModels.clear();
            observableList.clear();
        }
        preAccountId = accountId;
        showDialog();
        CocosBcxApiWrapper.getBcxInstance().get_all_account_balances(accountId, new IBcxCallBack() {
            private BigDecimal totalAssets = BigDecimal.ZERO;

            @Override
            public void onReceiveValue(final String s) {
                MainHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        AllAssetBalanceModel balanceEntity = GsonSingleInstance.getGsonInstance().fromJson(s, AllAssetBalanceModel.class);
                        if (balanceEntity.code != 1 || balanceEntity.getData().size() <= 0) {
                            dismissDialog();
                            emptyViewVisible.set(View.VISIBLE);
                            recyclerViewVisible.set(View.GONE);
                            return;
                        }
                        final List<AllAssetBalanceModel.DataBean> dataBeans = balanceEntity.getData();
                        for (int i = 0; i < dataBeans.size(); i++) {
                            //todo 价值计算
                            final AllAssetBalanceModel.DataBean dataBean = dataBeans.get(i);
                            totalAssets = totalAssets.add(dataBean.getAmount().multiply(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_DOWN));
                            totalAsset.set(String.valueOf(totalAssets));
                            final int finalI = i;
                            CocosBcxApiWrapper.getBcxInstance().lookup_asset_symbols(dataBean.getAsset_id(), new IBcxCallBack() {
                                @Override
                                public void onReceiveValue(final String s) {
                                    LogUtils.d("lookup_asset_symbols", s);
                                    final AssetsModel assetModel = GsonSingleInstance.getGsonInstance().fromJson(s, AssetsModel.class);
                                    if (assetModel.code != 1) {
                                        return;
                                    }
                                    MainHandler.getInstance().post(new Runnable() {
                                        @Override
                                        public void run() {
                                            AssetsModel.AssetModel assetModel1 = assetModel.getData();
                                            assetModel1.amount = dataBean.getAmount();
                                            if (assetModels.size() == dataBeans.size()) {
                                                if (!assetModel1.equals(assetModels.get(finalI))) {
                                                    assetModels.set(finalI, assetModel1);
                                                    AssetItemViewModel itemViewModel = new AssetItemViewModel(AssetViewModel.this, assetModel1);
                                                    observableList.set(finalI, itemViewModel);
                                                }
                                            } else {
                                                assetModels.add(assetModel1);
                                                AssetItemViewModel itemViewModel = new AssetItemViewModel(AssetViewModel.this, assetModel1);
                                                observableList.add(itemViewModel);
                                            }
                                            dismissDialog();
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


}