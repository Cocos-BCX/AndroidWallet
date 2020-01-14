package com.cocos.library_base.component.switch_account;

import android.content.ClipData;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.cocos.library_base.R;
import com.cocos.library_base.base.ItemViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.singleton.ClipboardManagerInstance;

import org.greenrobot.eventbus.EventBus;

/**
 * @author ningkang.guo
 * @Date 2019/2/12
 */
public class SwitchAccountItemViewModel extends ItemViewModel<SwitchAccountViewModel> {

    public ObservableField<String> accountNameBind = new ObservableField<>();
    public ObservableInt checkedVisible = new ObservableInt(View.INVISIBLE);
    public String accountName;

    public SwitchAccountItemViewModel(@NonNull SwitchAccountViewModel viewModel, String accountName) {
        super(viewModel);
        accountNameBind.set(accountName);
        this.accountName = accountName;
        checkedVisible.set(TextUtils.equals(accountName, AccountHelperUtils.getCurrentAccountName()) ? View.VISIBLE : View.INVISIBLE);
    }

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            checkedVisible.set(View.VISIBLE);
            AccountHelperUtils.setCurrentAccountName(accountName);

            AccountHelperUtils.setCurrentAccountName(accountName);
            EventBusCarrier eventBusCarrier1 = new EventBusCarrier();
            eventBusCarrier1.setEventType(EventTypeGlobal.DIALOG_DISMISS_TYPE);
            eventBusCarrier1.setObject(null);
            EventBus.getDefault().post(eventBusCarrier1);

            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.SWITCH_ACCOUNT);
            eventBusCarrier.setObject(null);
            EventBus.getDefault().post(eventBusCarrier);

        }
    });

    public BindingCommand copyClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ClipData mClipData = ClipData.newPlainText("Label", accountName);
            ClipboardManagerInstance.getClipboardManager().setPrimaryClip(mClipData);
            ToastUtils.showShort(R.string.copy_success);
        }
    });

}
