package com.cocos.module_asset.nh_order_manager;

import android.app.Application;
import android.content.ClipData;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.NhAssetOrderEntity;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.singleton.ClipboardManagerInstance;
import com.cocos.module_asset.R;

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


    public void requestOrderDetailData(NhAssetOrderEntity.NhOrderBean nhOrderBean) {
        orderId.set(nhOrderBean.id);
        nhAssetId.set(nhOrderBean.nh_asset_id);
        sellerAccount.set(nhOrderBean.isMineOrder ? AccountHelperUtils.getCurrentAccountName() : nhOrderBean.nh_asset_id);
        assetQualifier.set(nhOrderBean.asset_qualifier);
        worldView.set(nhOrderBean.world_view);
        baseDescribe.set(nhOrderBean.base_describe);
        price.set(nhOrderBean.priceWithSymbol);
        memo.set(nhOrderBean.memo);
        expirationTime.set(nhOrderBean.expirationTime);
        drawableImg = Utils.getDrawable(nhOrderBean.isMineOrder ? R.drawable.mine_nh_order_icon : R.drawable.all_nh_order_icon);
    }

    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });


    // 卖家账号复制
    public BindingCommand onSellerCopyClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ClipData mClipData = ClipData.newPlainText("Label", sellerAccount.get());
            ClipboardManagerInstance.getClipboardManager().setPrimaryClip(mClipData);
            ToastUtils.showShort(R.string.copy_success);
        }
    });

    // 基础数据复制
    public BindingCommand onBaseDesCopyClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ClipData mClipData = ClipData.newPlainText("Label", baseDescribe.get());
            ClipboardManagerInstance.getClipboardManager().setPrimaryClip(mClipData);
            ToastUtils.showShort(R.string.copy_success);
        }
    });
}
