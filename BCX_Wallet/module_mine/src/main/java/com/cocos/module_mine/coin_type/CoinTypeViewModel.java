package com.cocos.module_mine.coin_type;

import android.app.Application;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import com.cocos.library_base.base.BaseViewModel;
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
 * @Date 2019/2/22
 */
public class CoinTypeViewModel extends BaseViewModel {


    public CoinTypeViewModel(@NonNull Application application) {
        super(application);
        int currencyType = SPUtils.getInt(Utils.getContext(), SPKeyGlobal.CURRENCY_TYPE, 0);
        cnyCheck.set(currencyType == 0);
        usdCheck.set(currencyType == 1);
    }

    public ObservableBoolean cnyCheck = new ObservableBoolean(true);

    public ObservableBoolean usdCheck = new ObservableBoolean(false);


    //人民币按钮的点击事件
    public BindingCommand cnyOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            usdCheck.set(false);
            cnyCheck.set(true);
            SPUtils.putInt(Utils.getContext(), SPKeyGlobal.CURRENCY_TYPE, 0);
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.SWITCH_COIN_TYPE);
            eventBusCarrier.setObject(0);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });


    //美元的点击事件
    public BindingCommand usdOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            usdCheck.set(true);
            cnyCheck.set(false);
            SPUtils.putInt(Utils.getContext(), SPKeyGlobal.CURRENCY_TYPE, 1);
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.SWITCH_COIN_TYPE);
            eventBusCarrier.setObject(1);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });


    //取消按钮的点击事件
    public BindingCommand cancelOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.DIALOG_DISMISS_TYPE);
            eventBusCarrier.setObject(null);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });

}
