package com.cocos.module_mine.asset_operate.delete_nhasset;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.module_mine.entity.NHAssetModel;

import org.greenrobot.eventbus.EventBus;

/**
 * @author ningkang.guo
 * @Date 2019/7/19
 */
public class DeleteNhAssetViewModel extends BaseViewModel {

    public ObservableField<String> assetId = new ObservableField<>("");

    public ObservableField<String> worldView = new ObservableField<>("");

    public ObservableField<String> minerFee = new ObservableField<>("");

    public NHAssetModel.NHAssetModelBean nHAssetModelBean;

    public DeleteNhAssetViewModel(@NonNull Application application) {
        super(application);
    }

    public void setNhAssetModel(NHAssetModel.NHAssetModelBean nHAssetModelBean) {
        this.nHAssetModelBean = nHAssetModelBean;
        assetId.set(nHAssetModelBean.id);
        worldView.set(nHAssetModelBean.world_view);
        minerFee.set(nHAssetModelBean.minerFee + " " + nHAssetModelBean.feeSymbol);
    }


    //删除资产按钮
    public BindingCommand deleteConfirmOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.SHOW_DELETE_NH_ASSET_PASSWORD_VERIFY_DIALOG);
            eventBusCarrier.setObject(nHAssetModelBean);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });


    public BindingCommand dismissOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.DIALOG_DISMISS_TYPE);
            eventBusCarrier.setObject(nHAssetModelBean);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });
}
