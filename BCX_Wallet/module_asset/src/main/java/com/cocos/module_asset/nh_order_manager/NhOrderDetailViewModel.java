package com.cocos.module_asset.nh_order_manager;

import android.app.Application;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;

/**
 * @author ningkang.guo
 * @Date 2019/7/16
 */
public class NhOrderDetailViewModel extends BaseViewModel {


    public Drawable drawableImg;

    public ObservableField<String> orderId = new ObservableField<>("");
    public ObservableField<String> nhAssetId = new ObservableField<>("");
    public ObservableField<String> sellerAccount = new ObservableField<>("");
    public ObservableField<String> assetQualifier = new ObservableField<>("");
    public ObservableField<String> worldView = new ObservableField<>("");
    public ObservableField<String> baseDescribe = new ObservableField<>("");
    public ObservableField<String> price = new ObservableField<>("");
    public ObservableField<String> memo = new ObservableField<>("");
    public ObservableField<String> expirationTime = new ObservableField<>("");

    public NhOrderDetailViewModel(@NonNull Application application) {
        super(application);
    }


    // 卖家账号复制
    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });

    // 卖家账号复制
    public BindingCommand onSellerCopyClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });

    public void requestOrderDetailData() {

    }
}
