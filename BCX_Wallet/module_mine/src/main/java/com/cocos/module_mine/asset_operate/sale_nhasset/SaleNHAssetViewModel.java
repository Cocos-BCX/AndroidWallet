package com.cocos.module_mine.asset_operate.sale_nhasset;

import android.app.Application;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.view.View;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
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
    public ObservableField<String> saleMemo = new ObservableField<>("");
    public ObservableField<String> saleFee = new ObservableField<>("");
    public ObservableInt salePricesSymbolVisible = new ObservableInt(View.GONE);
    public ObservableInt choosePricesSymbolVisible = new ObservableInt(View.VISIBLE);

    public SaleNHAssetViewModel(@NonNull Application application) {
        super(application);
    }

    public class UIChangeObservable {
        public ObservableBoolean saleNHNBtnObservable = new ObservableBoolean(false);
    }


    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    public BindingCommand choosePriceSymbolCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    public BindingCommand saleNHNextOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.saleNHNBtnObservable.set(!uc.saleNHNBtnObservable.get());
        }
    });

    public void setNhAssetId(NHAssetModel.NHAssetModelBean nhAssetModelBean) {
        nhAssetId.set(nhAssetModelBean.id);
    }

}
