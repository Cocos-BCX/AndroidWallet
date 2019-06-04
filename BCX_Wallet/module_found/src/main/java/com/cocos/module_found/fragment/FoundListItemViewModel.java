package com.cocos.module_found.fragment;

import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.base.ItemViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.entity.FoundListModel;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.module_found.R;

import org.greenrobot.eventbus.EventBus;


/**
 * @author ningkang.guo
 * @Date 2019/2/27
 */
public class FoundListItemViewModel extends ItemViewModel {

    public FoundListModel foundlistModel;

    public Drawable listIcon;

    public ObservableField<String> listTitle = new ObservableField<>("");

    public ObservableField<String> listDesc = new ObservableField<>("");

    public FoundListItemViewModel(@NonNull BaseViewModel viewModel, FoundListModel foundlistModel) {
        super(viewModel);
        this.foundlistModel = foundlistModel;
        listTitle.set(foundlistModel.getListTitle());
        listDesc.set(foundlistModel.getListDesc());
        listIcon = ContextCompat.getDrawable(viewModel.getApplication(), foundlistModel.getListIcon());
    }

    public BindingCommand onItemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (TextUtils.isEmpty(foundlistModel.getListUrl())) {
                ToastUtils.showShort(R.string.module_found_to_be_expected);
                return;
            }
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.SHOW_FOUND_DIALOG);
            eventBusCarrier.setObject(foundlistModel);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });
}
