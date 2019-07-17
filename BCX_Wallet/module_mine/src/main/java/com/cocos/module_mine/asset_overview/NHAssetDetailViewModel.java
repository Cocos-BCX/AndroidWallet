package com.cocos.module_mine.asset_overview;

import android.app.Application;
import android.content.ClipData;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.singleton.ClipboardManagerInstance;
import com.cocos.module_mine.R;
import com.cocos.module_mine.entity.NHAssetModel;





/**
 * @author ningkang.guo
 * @Date 2019/7/16
 */
public class NHAssetDetailViewModel extends BaseViewModel {

    public Drawable drawableImg;
    public ObservableField<String> assetId = new ObservableField<>("");
    public ObservableField<String> nhAssetCreator = new ObservableField<>("");
    public ObservableField<String> ownerAccount = new ObservableField<>("");
    public ObservableField<String> assetQualifier = new ObservableField<>("");
    public ObservableField<String> worldView = new ObservableField<>("");
    public ObservableField<String> createTime = new ObservableField<>("");
    public ObservableField<String> baseDescription = new ObservableField<>("");
    public ObservableField<String> zoneData = new ObservableField<>("");

    public NHAssetDetailViewModel(@NonNull Application application) {
        super(application);

    }

    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    public BindingCommand onCreatorCopyClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ClipData mClipData = ClipData.newPlainText("Label", nhAssetCreator.get());
            ClipboardManagerInstance.getClipboardManager().setPrimaryClip(mClipData);
            ToastUtils.showShort(R.string.copy_success);
        }
    });

    public BindingCommand onOwnerCopyClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ClipData mClipData = ClipData.newPlainText("Label", ownerAccount.get());
            ClipboardManagerInstance.getClipboardManager().setPrimaryClip(mClipData);
            ToastUtils.showShort(R.string.copy_success);
        }
    });

    public BindingCommand baseDataCopyCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ClipData mClipData = ClipData.newPlainText("Label", baseDescription.get());
            ClipboardManagerInstance.getClipboardManager().setPrimaryClip(mClipData);
            ToastUtils.showShort(R.string.copy_success);
        }
    });

    public BindingCommand zoneDataCopyCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ClipData mClipData = ClipData.newPlainText("Label", zoneData.get());
            ClipboardManagerInstance.getClipboardManager().setPrimaryClip(mClipData);
            ToastUtils.showShort(R.string.copy_success);
        }
    });

    public BindingCommand transferNhAssetCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //todo
        }
    });

    public BindingCommand deleteNhAssetCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //todo
        }
    });

    public void requestAssetDetailData(NHAssetModel.NHAssetModelBean assetModelBean) {
        if (null == assetModelBean) {
            return;
        }
        drawableImg = ContextCompat.getDrawable(Utils.getContext(), R.drawable.nh_asset_detail_icon);
        assetId.set(assetModelBean.id);
        String owner = CocosBcxApiWrapper.getBcxInstance().get_account_name_by_id(assetModelBean.nh_asset_owner);
        String creator = CocosBcxApiWrapper.getBcxInstance().get_account_name_by_id(assetModelBean.nh_asset_creator);
        nhAssetCreator.set(creator);
        ownerAccount.set(owner);
        assetQualifier.set(assetModelBean.asset_qualifier);
        worldView.set(assetModelBean.world_view);
        createTime.set(assetModelBean.create_time);
        baseDescription.set(assetModelBean.base_describe);
        zoneData.set("");
    }
}
