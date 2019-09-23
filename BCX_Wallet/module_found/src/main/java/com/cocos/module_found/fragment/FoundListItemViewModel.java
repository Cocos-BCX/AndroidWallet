package com.cocos.module_found.fragment;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.base.ItemViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.entity.FoundListModel;
import com.cocos.library_base.entity.WebViewModel;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.global.SPKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.SPUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.module_found.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;


/**
 * @author ningkang.guo
 * @Date 2019/2/27
 */
public class FoundListItemViewModel extends ItemViewModel {

    public FoundListModel foundlistModel;

    public ObservableField<FoundListModel> entity = new ObservableField<>();

    public ObservableField<String> listTitle = new ObservableField<>("");

    public ObservableField<String> listDesc = new ObservableField<>("");

    public ObservableField<String> headerTitle = new ObservableField<>("");

    public ObservableInt headerTitleVisible = new ObservableInt(View.GONE);


    public FoundListItemViewModel(@NonNull BaseViewModel viewModel, FoundListModel foundlistModel) {
        super(viewModel);
        entity.set(foundlistModel);
        this.foundlistModel = foundlistModel;
        listTitle.set(foundlistModel.getListTitle());
        listDesc.set(foundlistModel.getListDesc());
        if (!TextUtils.isEmpty(foundlistModel.getHeaderTitle())) {
            headerTitleVisible.set(View.VISIBLE);
            headerTitle.set(foundlistModel.getHeaderTitle());
        }
    }

    public BindingCommand onItemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (TextUtils.isEmpty(foundlistModel.getLinkUrl())) {
                ToastUtils.showShort(R.string.module_found_to_be_expected);
                return;
            }
            ArrayList<String> urls = SPUtils.getUrlList(SPKeyGlobal.FOUND_DIALOG_SHOWED_MARK);
            if (null == urls || urls.size() <= 0) {
                urls = new ArrayList<>();
                SPUtils.setDataList(SPKeyGlobal.FOUND_DIALOG_SHOWED_MARK, urls);
                EventBusCarrier eventBusCarrier = new EventBusCarrier();
                eventBusCarrier.setEventType(EventTypeGlobal.SHOW_FOUND_DIALOG);
                eventBusCarrier.setObject(foundlistModel);
                EventBus.getDefault().post(eventBusCarrier);
                return;
            }
            if (urls.contains(foundlistModel.getLinkUrl())) {
                WebViewModel webViewModel = new WebViewModel();
                webViewModel.setTitle(foundlistModel.getListTitle());
                webViewModel.setUrl(foundlistModel.getLinkUrl());
                Bundle bundle = new Bundle();
                bundle.putSerializable(IntentKeyGlobal.WEB_MODEL, webViewModel);
                ARouter.getInstance().build(RouterActivityPath.ACTIVITY_JS_WEB).with(bundle).navigation();
            } else {
                SPUtils.setDataList(SPKeyGlobal.FOUND_DIALOG_SHOWED_MARK, urls);
                EventBusCarrier eventBusCarrier = new EventBusCarrier();
                eventBusCarrier.setEventType(EventTypeGlobal.SHOW_FOUND_DIALOG);
                eventBusCarrier.setObject(foundlistModel);
                EventBus.getDefault().post(eventBusCarrier);
                SPUtils.setDataList(SPKeyGlobal.FOUND_DIALOG_SHOWED_MARK, urls);
            }
        }
    });
}
