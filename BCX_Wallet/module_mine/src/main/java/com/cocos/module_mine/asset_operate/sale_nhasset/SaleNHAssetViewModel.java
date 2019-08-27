package com.cocos.module_mine.asset_operate.sale_nhasset;

import android.app.Application;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_wallet.chain.global_property_object;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.utils.Utils;
import com.cocos.module_mine.R;
import com.cocos.module_mine.entity.NHAssetModel;

/**
 * @author ningkang.guo
 * @Date 2019/7/17
 */

public class SaleNHAssetViewModel extends BaseViewModel {

    public UIChangeObservable uc = new UIChangeObservable();
    public ObservableField<String> nhAssetId = new ObservableField<>("");
    public ObservableField<String> salePricesSymbol = new ObservableField<>("COCOS");
    public ObservableField<String> salePricesAmount = new ObservableField<>("");
    public ObservableField<String> saleValidTime = new ObservableField<>("");
    public ObservableField<String> saleValidTimeDefault = new ObservableField<>("");
    public long saleValidTimeMax = 0;
    public ObservableField<String> saleMemo = new ObservableField<>("");

    public SaleNHAssetViewModel(@NonNull Application application) {
        super(application);
    }

    public class UIChangeObservable {
        public ObservableBoolean saleNHNBtnObservable = new ObservableBoolean(false);
        public ObservableBoolean choosePriceSymbolObservable = new ObservableBoolean(false);
    }

    public BindingCommand backOnClickCommand = new BindingCommand(() -> finish());

    public BindingCommand choosePriceSymbolCommand = new BindingCommand(() -> uc.choosePriceSymbolObservable.set(!uc.choosePriceSymbolObservable.get()));

    public BindingCommand saleNHNextOnClickCommand = new BindingCommand(() -> uc.saleNHNBtnObservable.set(!uc.saleNHNBtnObservable.get()));

    public void setNhAssetId(NHAssetModel.NHAssetModelBean nhAssetModelBean) {
        nhAssetId.set(nhAssetModelBean.id);
        global_property_object global_property_object = CocosBcxApiWrapper.getBcxInstance().get_global_properties();
        if (null != global_property_object) {
            saleValidTimeMax = global_property_object.parameters.maximum_nh_asset_order_expiration - 100;
            saleValidTimeDefault.set(Utils.getString(R.string.module_mine_nh_asset_sale_valid_time_max) + (global_property_object.parameters.maximum_nh_asset_order_expiration - 100) + " " + Utils.getString(R.string.module_mine_nh_asset_sale_valid_time_default));
        }
    }

}
