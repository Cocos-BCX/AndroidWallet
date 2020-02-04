package com.cocos.library_base.viewmodel;

import android.app.Application;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.global.EventTypeGlobal;

import org.greenrobot.eventbus.EventBus;

public class JsWebMoreViewModel extends BaseViewModel {

    public JsWebMoreViewModel(@NonNull Application application) {
        super(application);
    }

    public BindingCommand refreshOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.JSWEB_REFRESH_TYPE);
            eventBusCarrier.setObject(null);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });

    public BindingCommand copyUrlOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.JSWEB_COPYURL_TYPE);
            eventBusCarrier.setObject(null);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });

    public BindingCommand shareOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.JSWEB_SHARE_TYPE);
            eventBusCarrier.setObject(null);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });

    public BindingCommand browserOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.JSWEB_BROWSER_TYPE);
            eventBusCarrier.setObject(null);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });

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
