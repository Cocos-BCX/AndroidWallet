package com.cocos.module_mine.asset_operate.sale_nhasset;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.cocos.bcx_sdk.bcx_wallet.chain.asset_object;
import com.cocos.library_base.base.ItemViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.global.SPKeyGlobal;
import com.cocos.library_base.utils.SPUtils;
import com.cocos.library_base.utils.Utils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author ningkang.guo
 * @Date 2019/7/18
 */
public class SymbolChooseItemViewModel extends ItemViewModel {


    public asset_object asset_object;
    public ObservableField<String> symbol = new ObservableField<>("");
    public ObservableField<String> symbolID = new ObservableField<>("");
    public ObservableBoolean symbolChecked = new ObservableBoolean(false);

    public SymbolChooseItemViewModel(SymbolChooseViewModel viewModel, asset_object asset_object) {
        super(viewModel);
        this.asset_object = asset_object;
        symbol.set(asset_object.symbol);
        symbolID.set("ID：" + asset_object.id);
        String selectedSymbol = SPUtils.getString(Utils.getContext(), SPKeyGlobal.SYMBOL_SELECTED);
        if (TextUtils.isEmpty(selectedSymbol)) {
            symbolChecked.set(TextUtils.equals("COCOS", asset_object.symbol));
        } else {
            symbolChecked.set(TextUtils.equals(selectedSymbol, asset_object.symbol));
        }
    }

    public BindingCommand symbolCheckedOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            symbolChecked.set(true);
            if (symbolChecked.get()) {
                EventBusCarrier eventBusCarrier = new EventBusCarrier();
                eventBusCarrier.setEventType(EventTypeGlobal.SET_PRICE_SYMBOL);
                eventBusCarrier.setObject(asset_object.symbol);
                EventBus.getDefault().post(eventBusCarrier);
                SPUtils.putString(Utils.getContext(), SPKeyGlobal.SYMBOL_SELECTED, asset_object.symbol);
            }
        }
    });

}
