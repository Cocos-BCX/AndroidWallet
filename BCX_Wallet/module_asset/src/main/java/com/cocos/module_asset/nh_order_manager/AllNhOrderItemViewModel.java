package com.cocos.module_asset.nh_order_manager;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.cocos.library_base.base.ItemViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.module_asset.entity.NhOrderEntity;

/**
 * @author ningkang.guo
 * @Date 2019/7/15
 */
public class AllNhOrderItemViewModel extends ItemViewModel {

    public AllNhOrderItemViewModel(@NonNull AllNhOrderViewModel viewModel, NhOrderEntity nhOrderEntity) {
        super(viewModel);
    }

    public ObservableField<String> allNhOrderId = new ObservableField<>("4.2.135");
    public ObservableField<String> allNhOrderPrice = new ObservableField<>("66 COCOS");
    public ObservableField<String> nhAssetIconUrl = new ObservableField<>("http://47.75.186.60/itemimgs/bomb.png");
    public ObservableField<String> nhAssetId = new ObservableField<>("4.32.1");
    public ObservableField<String> allNhOrderSeller = new ObservableField<>("gnkhandsome1");
    public ObservableField<String> allNhOrderExpritationTime = new ObservableField<>("过期时间：2019.10.26");
    public ObservableField<String> allNhOrderMemo = new ObservableField<>("texgs");

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });

    //取消订单按钮
    public BindingCommand buyNhOrderCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });
}
