package com.cocos.module_mine.asset_overview;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.bcx_sdk.bcx_log.LogUtils;
import com.cocos.library_base.base.ItemViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.NHBaseDataEntity;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.widget.zloading.text.TextBuilder;
import com.cocos.module_mine.R;
import com.cocos.module_mine.entity.NHAssetModel;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ningkang.guo
 * @Date 2019/2/25
 */
public class NHAssetItemViewModel extends ItemViewModel {

    //public Drawable nhAssetIcon;
    public ObservableField<String> nhAssetId = new ObservableField<>("");
    public ObservableField<String> assetQualifier = new ObservableField<>("");
    public ObservableField<String> assetWorldView = new ObservableField<>("");
    public ObservableField<String> nhAssetIconUrl = new ObservableField<>("");
    public ObservableInt placeholderRes = new ObservableInt();
    public NHAssetModel.NHAssetModelBean entity;

    public NHAssetItemViewModel(@NonNull NHAssetViewModel viewModel, NHAssetModel.NHAssetModelBean NHAssetModelBean) {
        super(viewModel);
        try {
            this.entity = NHAssetModelBean;
            nhAssetId.set("ID：" + NHAssetModelBean.id);
            assetQualifier.set(NHAssetModelBean.asset_qualifier);
            assetWorldView.set(NHAssetModelBean.world_view);
            placeholderRes.set(R.drawable.nh_asset_icon);
//        nhAssetIcon = ContextCompat.getDrawable(viewModel.getApplication(), R.drawable.nh_asset_icon);
            if (!TextUtils.isEmpty(entity.base_describe)) {
                NHBaseDataEntity kittyBase = GsonSingleInstance.buildGson().fromJson(entity.base_describe, NHBaseDataEntity.class);
                if (!TextUtils.isEmpty(kittyBase.icon)) {
                    nhAssetIconUrl.set(kittyBase.icon);
                }

            }
        } catch (Exception e) {
        }
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
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKeyGlobal.NH_ASSET_MODEL, entity);
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_SALE_NH_ASSET).with(bundle).navigation();
        }
    });
}
