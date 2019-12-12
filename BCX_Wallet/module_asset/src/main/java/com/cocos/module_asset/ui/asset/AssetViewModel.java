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
import java.util.List;

import me.tatarka.bindingcollectionadapter2.BR;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @author ningkang.guo
 */
public class AssetViewModel extends BaseViewModel {

    public ObservableInt emptyViewVisible = new ObservableInt(View.GONE);

    public ObservableInt LoginViewVisible = new ObservableInt(View.INVISIBLE);

    public ObservableInt recyclerViewVisible = new ObservableInt(View.VISIBLE);

    public ObservableInt accountViewVisible = new ObservableInt(View.INVISIBLE);


    int tryCount = 0;

    public AssetViewModel(@NonNull Application application) {
        super(application);
    }

    public String accountName;

    public void setAccountName() {
        this.accountName = AccountHelperUtils.getCurrentAccountName();
        currentAccountName.set(accountName);
        accountViewVisible.set(TextUtils.isEmpty(accountName) ? View.INVISIBLE : View.VISIBLE);
    }

    //当前帐户名户名的绑定
    public ObservableField<String> currentAccountName = new ObservableField<>();

    //当前资产显示的货币单位
    public ObservableField<String> totalAssetCurrencyUnit = new ObservableField<>(Utils.getString(R.string.module_asset_total_assets));

    //当前帐户总资产
    public ObservableField<String> totalAsset = new ObservableField<>("0.00");

    public ObservableField<String> loginText = new ObservableField<>(Utils.getString(R.string.module_asset_fragment_asset_login_text));

    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public ObservableBoolean accountItemObservable = new ObservableBoolean(false);
    }

    //登陆注册按钮的点击事件
    public BindingCommand loginViewClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_PASSWORD_LOGIN).navigation();
        }
    });


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
            webViewModel.setUrl(Utils.getString(R.string.module_asset_sources_url));
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
            webViewModel.setUrl(Utils.getString(R.string.module_asset_vote_url));
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKeyGlobal.WEB_MODEL, webViewModel);
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_JS_WEB).with(bundle).navigation();
        }
    });


    public ObservableList<AssetItemViewModel> observableList = new ObservableArrayList<>();
    public ItemBinding<AssetItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.module_asset_item_assets);
    public final BindingRecyclerViewAdapter<AssetItemViewModel> adapter = new BindingRecyclerViewAdapter<>();


    public void requestAssetsListData() {
        try {
            final String accountId = AccountHelperUtils.getCurrentAccountId();
            // 如果不是同一个账号则清除数据
            if (TextUtils.isEmpty(accountId)) {
                emptyViewVisible.set(View.VISIBLE);
                recyclerViewVisible.set(View.GONE);
                LoginViewVisible.set(View.GONE);
                return;
            }
            showDialog();
            CocosBcxApiWrapper.getBcxInstance().get_all_account_balances(accountId, new IBcxCallBack() {
                private BigDecimal totalAssets = BigDecimal.ZERO;

                @Override
                public void onReceiveValue(final String s) {
                    LogUtils.d("get_all_account_balances", s);
                    final AllAssetBalanceModel balanceEntity = GsonSingleInstance.getGsonInstance().fromJson(s, AllAssetBalanceModel.class);
                    MainHandler.getInstance().post(new Runnable() {
                        @Override
                        public void run() {
                            if (balanceEntity.code != 1 || balanceEntity.getData().size() <= 0) {
                                LogUtils.d("emptyViewVisible", "emptyViewVisible");
                                if (tryCount < 3) {
                                    requestAssetsListData();
                                    ++tryCount;
                                    LogUtils.d("hasTryAgain", "hasTryAgain");
                                } else {
                                    dismissDialog();
                                    emptyViewVisible.set(View.VISIBLE);
                                    recyclerViewVisible.set(View.GONE);
                                    LoginViewVisible.set(View.GONE);
                                }
                                return;
                            }
                            final List<AllAssetBalanceModel.DataBean> dataBeans = balanceEntity.getData();
                            observableList.clear();
                            for (int i = 0; i < dataBeans.size(); i++) {
                                //todo 价值计算
                                final AllAssetBalanceModel.DataBean dataBean = dataBeans.get(i);
                                totalAssets = totalAssets.add(dataBean.getAmount().multiply(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_DOWN));
                                totalAsset.set(String.valueOf(totalAssets));
                                if (TextUtils.equals(dataBean.getAsset_id(), "1.3.1")) {
                                    continue;
                                }
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
                                                AssetItemViewModel itemViewModel = new AssetItemViewModel(AssetViewModel.this, assetModel1);
                                                if (TextUtils.equals(assetModel1.symbol, "COCOS")) {
                                                    observableList.add(0, itemViewModel);
                                                } else {
                                                    observableList.add(itemViewModel);
                                                }
                                                dismissDialog();
                                                emptyViewVisible.set(View.GONE);
                                                recyclerViewVisible.set(View.VISIBLE);
                                                LoginViewVisible.set(View.GONE);
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
        }
    }


}