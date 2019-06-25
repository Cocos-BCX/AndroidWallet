package com.cocos.module_asset.ui.receivables;

import android.app.Application;
import android.content.ClipData;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.binding.command.BindingConsumer;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.entity.AssetsModel;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.singleton.ClipboardManagerInstance;
import com.cocos.module_asset.R;
import com.cocos.module_zxing.utils.QRCodeHelper;

import org.greenrobot.eventbus.EventBus;

/**
 * @author ningkang.guo
 * @Date 2019/2/18
 */
public class ReceivablesViewModel extends BaseViewModel {

    public Drawable qrCodeDrawable;
    public AssetsModel.AssetModel assetModel;

    public ReceivablesViewModel(@NonNull Application application) {
        super(application);
    }

    //收款代币名称的绑定
    public ObservableField<String> receivablesTokenName = new ObservableField<>("");
    //收款金额的绑定
    public ObservableField<String> receivableAmount = new ObservableField<>("0.00");
    //收款账户名称的绑定
    public ObservableField<String> receivableAccountName = new ObservableField<>(AccountHelperUtils.getCurrentAccountName());

    public ObservableField<String> symbolType = new ObservableField<>(Utils.getString(R.string.module_asset_coin_type_test));

    //返回按钮事件
    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    //复制账户名称
    public BindingCommand copyAccountNameOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (TextUtils.isEmpty(receivableAccountName.get())) {
                return;
            }
            ClipData mClipData = ClipData.newPlainText("Label", receivableAccountName.get());
            ClipboardManagerInstance.getClipboardManager().setPrimaryClip(mClipData);
            ToastUtils.showShort(R.string.copy_success);
        }
    });

    // 金额输入框变动监听
    public BindingCommand<String> onReceivableAmountChanged = new BindingCommand<>(new BindingConsumer<String>() {
        @Override
        public void call(String text) {
            receivableAmount.set(text);
            refreshQrCodeImage();
        }
    });

    private void refreshQrCodeImage() {
        try {
            String message = "{\"address\":\"%s\",\"amount\":\"%s\",\"symbol\":\"%s\"}";
            message = String.format(message, receivableAccountName.get(), receivableAmount.get(), receivablesTokenName.get());
            Bitmap qrBitmap = QRCodeHelper.generateBitmap(message, Utils.dip2px(199), Utils.dip2px(199));
            qrCodeDrawable = new BitmapDrawable(qrBitmap);
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.RECEIVE_QRCODE_CHANGE);
            eventBusCarrier.setObject(null);
            EventBus.getDefault().post(eventBusCarrier);
        } catch (Exception e) {

        }
    }

    public void setAssetModel(AssetsModel.AssetModel assetModel) {
        this.assetModel = assetModel;
        receivablesTokenName.set(assetModel.symbol);
        refreshQrCodeImage();
    }
}
