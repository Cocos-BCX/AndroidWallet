package com.cocos.module_mine.contact;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.library_base.base.ItemViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.entity.ContactModel;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;

import org.greenrobot.eventbus.EventBus;

/**
 * @author ningkang.guo
 * @Date 2019/2/12
 */
public class ContactItemViewModel extends ItemViewModel<ContactViewModel> {

    public ObservableField<ContactModel> entity = new ObservableField<>();
    public String memoshow;
    public ContactModel contactModel;
    private int type;

    public ContactItemViewModel(@NonNull ContactViewModel viewModel, ContactModel entity, int type) {
        super(viewModel);
        this.type = type;
        this.contactModel = entity;
        this.entity.set(entity);
        if (TextUtils.isEmpty(entity.getMemo())) {
            memoshow = "";
            return;
        }
        memoshow = "(" + entity.getMemo() + ")";
    }

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (IntentKeyGlobal.GET_CONTACT == type) {
                EventBusCarrier eventBusCarrier = new EventBusCarrier();
                eventBusCarrier.setEventType(EventTypeGlobal.SET_CONTACT);
                eventBusCarrier.setObject(contactModel);
                EventBus.getDefault().post(eventBusCarrier);
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKeyGlobal.CONTACT_ENTITY, contactModel);
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_EDIT_CONTACT).with(bundle).navigation();
        }
    });
}
