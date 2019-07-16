package com.cocos.module_asset.nh_order_manager;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.base.ItemViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.module_asset.entity.NhOrderEntity;

/**
 * @author ningkang.guo
 * @Date 2019/7/15
 */
public class MineNhOrderItemViewModel extends ItemViewModel {



    public ObservableField<String> mineNhOrderId = new ObservableField<>("4.2.135");
    public ObservableField<String> mineNhOrderPrice = new ObservableField<>("66 COCOS");
    public ObservableField<String> nhAssetIconUrl = new ObservableField<>("http://47.75.186.60/itemimgs/bomb.png");
    public ObservableField<String> nhAssetId = new ObservableField<>("4.32.1");
    public ObservableField<String> mineNhOrderExpritationTime = new ObservableField<>("过期时间：2019.10.26");
    public ObservableField<String> mineNhOrderMemo = new ObservableField<>("texgs");


    public MineNhOrderItemViewModel(@NonNull MineNhOrderViewModel viewModel, NhOrderEntity nhOrderEntity) {
        super(viewModel);
    }

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });

    //取消订单按钮
    public BindingCommand cancelNhOrderCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });
}
