package com.cocos.module_mine.asset_overview;

import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.base.ItemViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.AssetsModel;

/**
 * @author ningkang.guo
 * @Date 2019/2/20
 */
public class RecyclerAssetItemViewModel extends ItemViewModel {

    public ObservableField<AssetsModel.AssetModel> entity = new ObservableField<>();
    public Drawable drawableImg;
    public ObservableField<String> totalValue = new ObservableField<>("0.00");
    public ObservableField<String> amount = new ObservableField<>("0.00");

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });

    public RecyclerAssetItemViewModel(@NonNull BaseViewModel viewModel) {
        super(viewModel);
    }
}
