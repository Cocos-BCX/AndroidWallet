package com.cocos.module_mine.asset_overview;

import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.library_base.base.ItemViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.module_mine.R;
import com.cocos.module_mine.entity.NHAssetModel;
import com.cocos.module_mine.entity.NHUserInfoModel;

/**
 * @author ningkang.guo
 * @Date 2019/2/25
 */
public class NHAssetItemViewModel extends ItemViewModel {

    public Drawable nhAssetIcon;
    public Object url;
    public ObservableField<String> nhAssetId = new ObservableField<>("");
    public ObservableField<String> assetQualifier = new ObservableField<>("");
    public ObservableField<String> assetWorldView = new ObservableField<>("");
    public NHAssetModel.NHAssetModelBean entity;

    public NHAssetItemViewModel(@NonNull NHAssetViewModel viewModel, NHAssetModel.NHAssetModelBean NHAssetModelBean) {
        super(viewModel);
        this.entity = NHAssetModelBean;
        String showName ="ID：" + NHAssetModelBean.id;
        String iconUrl=null ;
        String base_describe = NHAssetModelBean.base_describe;
        if (base_describe!=null){
            NHUserInfoModel nhUserInfoModel = GsonSingleInstance.getGsonInstance().fromJson(base_describe, NHUserInfoModel.class);
            if (nhUserInfoModel!=null){
                String icon = nhUserInfoModel.getIcon();
                if (!TextUtils.isEmpty(icon)){
                    iconUrl= icon;
                }
                String name = nhUserInfoModel.getName();
                if (!TextUtils.isEmpty(name)){
                    showName = "name：" + name;
                }

            }
        }
        if (iconUrl==null){
            url = ContextCompat.getDrawable(viewModel.getApplication(), R.drawable.nh_asset_icon);
        }else {
            url = iconUrl;
        }
        nhAssetId.set(showName);
        assetQualifier.set(NHAssetModelBean.asset_qualifier);
        assetWorldView.set(NHAssetModelBean.world_view);
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
