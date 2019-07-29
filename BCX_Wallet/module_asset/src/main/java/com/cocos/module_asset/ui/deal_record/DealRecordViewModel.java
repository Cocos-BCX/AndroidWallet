package com.cocos.module_asset.ui.deal_record;

import android.app.Application;
import android.content.ClipData;
import android.databinding.ObservableArrayList;
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
import com.cocos.bcx_sdk.bcx_wallet.chain.global_config_object;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.AssetsModel;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.singleton.ClipboardManagerInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_asset.BR;
import com.cocos.module_asset.R;
import com.cocos.module_asset.entity.DealRecordModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @author ningkang.guo
 * @Date 2019/2/20
 */
public class DealRecordViewModel extends BaseViewModel {

    public DealRecordViewModel(@NonNull Application application) {
        super(application);
    }

    private AssetsModel.AssetModel assetModel;

    public ObservableInt emptyViewVisible = new ObservableInt(View.GONE);

    public ObservableInt recyclerViewVisible = new ObservableInt(View.VISIBLE);

    public ObservableField<String> symbolType = new ObservableField<>(Utils.getString(R.string.module_asset_coin_type_test));

    //资产名称
    public ObservableField<String> tokenSymbol = new ObservableField<>("COCOS");

    //总资产
    public ObservableField<String> totalAsset = new ObservableField<>();

    public ObservableField<String> totalAssetValue = new ObservableField<>("≈ ￥0.00");

    public ObservableField<String> accountName = new ObservableField<>("");

    //返回按钮的点击事件
    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    //复制帐户名按钮的点击事件
    public BindingCommand copyOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (TextUtils.isEmpty(accountName.get())) {
                return;
            }
            ClipData mClipData = ClipData.newPlainText("Label", accountName.get());
            ClipboardManagerInstance.getClipboardManager().setPrimaryClip(mClipData);
            ToastUtils.showShort(R.string.copy_success);
        }
    });

    //转账按钮的点击事件
    public BindingCommand transferClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKeyGlobal.ASSET_MODEL, assetModel);
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_TRANSFER).with(bundle).navigation();
        }
    });

    //收款按钮的点击事件
    public BindingCommand receivablesOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKeyGlobal.ASSET_MODEL, assetModel);
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_RECEIVABLES).with(bundle).navigation();
        }
    });

    public void setAssetModel(AssetsModel.AssetModel assetModel) {
        this.assetModel = assetModel;
        tokenSymbol.set(assetModel.symbol);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        totalAsset.set(nf.format(assetModel.amount.setScale(5, RoundingMode.HALF_UP).add(BigDecimal.ZERO)));
        totalAssetValue.set("≈ ￥" + assetModel.amount.multiply(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP));
        accountName.set(String.valueOf(AccountHelperUtils.getCurrentAccountName()));
    }

    public ObservableList<DealRecordItemViewModel> observableList = new ObservableArrayList<>();

    public ItemBinding<DealRecordItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.module_asset_deal_record_item);

    public final BindingRecyclerViewAdapter<DealRecordItemViewModel> adapter = new BindingRecyclerViewAdapter<>();

    public void requestDealRecordList() {
        showDialog();
        CocosBcxApiWrapper.getBcxInstance().get_account_history(accountName.get(), 100, new IBcxCallBack() {
            @Override
            public void onReceiveValue(final String value) {
                MainHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        DealRecordModel dealRecordModel = global_config_object.getInstance().getGsonBuilder().create().fromJson(value, DealRecordModel.class);
                        observableList.clear();
                        if (!dealRecordModel.isSuccess() || dealRecordModel.data.size() <= 0) {
                            return;
                        }
                        for (DealRecordModel.DealRecordItemModel recordItemModel : dealRecordModel.getData()) {
                            double option = (double) recordItemModel.op.get(0);
                            if (option == 0 || option == 44 || option == 51) {
                                DealRecordItemViewModel itemViewModel = new DealRecordItemViewModel(DealRecordViewModel.this, recordItemModel);
                                observableList.add(itemViewModel);
                            }
                        }
                        dismissDialog();
                    }
                });
            }
        });
    }

}


