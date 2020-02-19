package com.cocos.library_base.base;

import android.app.Application;
import android.support.annotation.NonNull;

import com.cocos.library_base.R;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.invokedpages.model.BaseInfo;
import com.cocos.library_base.invokedpages.model.BaseInvokeModel;
import com.cocos.library_base.utils.IntentUtils;
import com.cocos.library_base.utils.Utils;

import org.greenrobot.eventbus.EventBus;

public class ConfrimDialogViewModel extends BaseViewModel {

    public ConfrimDialogViewModel(@NonNull Application application) {
        super(application);
    }

    BaseInfo baseInfo;
    BaseInvokeModel baseInvokeModel;
    public BindingCommand cancelOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.DIALOG_DISMISS_TYPE);
            eventBusCarrier.setObject(null);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });


    public BindingCommand confirmOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.DIALOG_DISMISS_TYPE);
            eventBusCarrier.setObject(null);
            EventBus.getDefault().post(eventBusCarrier);
//            IntentUtils.jumpToDappWithError(Utils.getContext(), baseInvokeModel, baseInfo.getActionId(), Utils.getString(R.string.author_account_not_exist));
        }
    });

    public void setBaseInfo(BaseInvokeModel baseInvokeModel, BaseInfo baseInfo) {
        this.baseInvokeModel = baseInvokeModel;
        this.baseInfo = baseInfo;
    }
}
