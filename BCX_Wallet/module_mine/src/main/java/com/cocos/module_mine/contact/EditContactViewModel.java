package com.cocos.module_mine.contact;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.entity.ContactModel;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.singleton.ContactDaoInstance;
import com.cocos.module_mine.R;

import org.greenrobot.eventbus.EventBus;

/**
 * @author ningkang.guo
 * @Date 2019/2/19
 */
public class EditContactViewModel extends BaseViewModel {

    ClipboardManager mClipboardManager;

    public EditContactViewModel(@NonNull Application application) {
        super(application);
        mClipboardManager = (ClipboardManager) application.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    //联系人名称绑定
    public ObservableField<String> contactName = new ObservableField<>();

    //联系人备注绑定
    public ObservableField<String> contactMemo = new ObservableField<>();

    //联系人帐户名绑定
    public ObservableField<String> contactAccountName = new ObservableField<>();

    public ObservableInt delete = new ObservableInt(View.INVISIBLE);

    public ObservableInt copy = new ObservableInt(View.INVISIBLE);


    //返回按钮
    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    //完成按钮
    public BindingCommand complishOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            if (TextUtils.isEmpty(contactName.get())) {
                ToastUtils.showShort(R.string.module_mine_name_empty);
                return;
            }

            if (TextUtils.isEmpty(contactAccountName.get())) {
                ToastUtils.showShort(R.string.module_mine_contact_address);
                return;
            }
            ContactDaoInstance.getContactDaoInstance().insertContact(contactName.get(), contactAccountName.get(), contactMemo.get());
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.CONTACT_CHANGED_TYPE);
            eventBusCarrier.setObject(null);
            EventBus.getDefault().post(eventBusCarrier);
            finish();
        }
    });

    //删除按钮
    public BindingCommand deleteContactOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ContactDaoInstance.getContactDaoInstance().deleteContact(contactAccountName.get());
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.CONTACT_CHANGED_TYPE);
            eventBusCarrier.setObject(null);
            EventBus.getDefault().post(eventBusCarrier);
            ToastUtils.showShort(R.string.module_mine_contact_delete_success);
            finish();
        }
    });

    //复制按钮
    public BindingCommand copyOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ClipData mClipData = ClipData.newPlainText("Label", contactAccountName.get());
            mClipboardManager.setPrimaryClip(mClipData);
            ToastUtils.showShort(R.string.copy_success);
        }
    });

    public void setContactModel(ContactModel contactModel) {
        if (null != contactModel) {
            contactName.set(contactModel.name);
            contactMemo.set(contactModel.memo);
            contactAccountName.set(contactModel.accountName);
            delete.set(View.VISIBLE);
            copy.set(View.VISIBLE);
        } else {
            delete.set(View.INVISIBLE);
            copy.set(View.INVISIBLE);
        }
    }
}
