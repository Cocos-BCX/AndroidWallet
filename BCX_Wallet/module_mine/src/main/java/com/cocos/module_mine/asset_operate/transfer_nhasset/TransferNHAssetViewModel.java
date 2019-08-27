package com.cocos.module_mine.asset_operate.transfer_nhasset;

import android.app.Application;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.module_mine.entity.NHAssetModel;

/**
 * @author ningkang.guo
 * @Date 2019/7/22
 */
public class TransferNHAssetViewModel extends BaseViewModel {


    public ObservableField<String> nhAssetReciver = new ObservableField<>("");

    public ObservableField<String> nhAssetId = new ObservableField<>("");

    public TransferNHAssetViewModel(@NonNull Application application) {
        super(application);
    }

    public UIChangeObservable uc = new UIChangeObservable();

    public void setNhAsset(NHAssetModel.NHAssetModelBean nHAssetModelBean) {
        nhAssetId.set(nHAssetModelBean.id);
    }

    public class UIChangeObservable {
        public ObservableBoolean transferNhBtnObservable = new ObservableBoolean(false);
        public ObservableBoolean toContact = new ObservableBoolean(false);
        public ObservableBoolean toCaptureActivity = new ObservableBoolean(false);
    }

    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });


    public BindingCommand scanOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.toCaptureActivity.set(!uc.toCaptureActivity.get());
        }
    });

    public BindingCommand contactOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.toContact.set(!uc.toContact.get());
        }
    });


    public BindingCommand transferNHNextOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.transferNhBtnObservable.set(!uc.transferNhBtnObservable.get());
        }
    });

}
