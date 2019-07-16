package com.cocos.module_mine.asset_overview;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.cocos.library_base.base.ItemViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.module_mine.entity.PropAssetModel;

/**
 * @author ningkang.guo
 * @Date 2019/2/25
 */
public class PropAssetItemViewModel extends ItemViewModel {

    public ObservableField<PropAssetModel.PropAssetModelBean> entity = new ObservableField<>();
    public ObservableField<String> nhAssetIconUrl = new ObservableField<>("http://47.75.186.60/itemimgs/bomb.png");
    public ObservableField<String> nhAssetId = new ObservableField<>("");
    public ObservableField<String> assetQualifier = new ObservableField<>("");
    public ObservableField<String> assetWorldView = new ObservableField<>("");

    public PropAssetItemViewModel(@NonNull PropAssetViewModel viewModel, PropAssetModel.PropAssetModelBean entity) {
        super(viewModel);
        this.entity.set(entity);
        nhAssetId.set(entity.id);
        assetQualifier.set(entity.asset_qualifier);
        assetWorldView.set(entity.world_view);
    }

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });

    //条目的点击事件
    public BindingCommand deleteNhAssetCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });
}
