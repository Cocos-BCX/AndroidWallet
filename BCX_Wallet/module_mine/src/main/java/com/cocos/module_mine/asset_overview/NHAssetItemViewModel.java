package com.cocos.module_mine.asset_overview;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.library_base.base.ItemViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.module_mine.entity.PropAssetModel;

import org.greenrobot.eventbus.EventBus;

/**
 * @author ningkang.guo
 * @Date 2019/2/25
 */
public class NHAssetItemViewModel extends ItemViewModel {

    public ObservableField<String> nhAssetIconUrl = new ObservableField<>("http://47.75.186.60/itemimgs/bomb.png");
    public ObservableField<String> nhAssetId = new ObservableField<>("");
    public ObservableField<String> assetQualifier = new ObservableField<>("");
    public ObservableField<String> assetWorldView = new ObservableField<>("");
    public PropAssetModel.PropAssetModelBean entity;

    public NHAssetItemViewModel(@NonNull NHAssetViewModel viewModel, PropAssetModel.PropAssetModelBean propAssetModelBean) {
        super(viewModel);
        this.entity = propAssetModelBean;
        nhAssetId.set(propAssetModelBean.id);
        assetQualifier.set(propAssetModelBean.asset_qualifier);
        assetWorldView.set(propAssetModelBean.world_view);
    }

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKeyGlobal.NH_ASSET_MODEL, entity);
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_NH_ASSET_DETAIL).with(bundle).navigation();
        }
    });

    public BindingCommand saleNhAssetCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.SHOW_NH_ASSET_DELETE_CONFIRM_DIALOG);
            eventBusCarrier.setObject(entity);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });
}
