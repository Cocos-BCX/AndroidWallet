package com.cocos.module_asset.ui.nh_transfer_detail;

import android.app.Application;
import android.content.ClipData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.view.View;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.singleton.ClipboardManagerInstance;
import com.cocos.module_asset.R;
import com.cocos.module_asset.entity.DealDetailModel;

/**
 * @author ningkang.guo
 * @Date 2019/2/20
 */
public class NHTransferDealDetailViewModel extends BaseViewModel {

    public NHTransferDealDetailViewModel(@NonNull Application application) {
        super(application);
    }


    public DealDetailModel dealDetailModel;

    public ObservableField<String> dealType = new ObservableField<>("");
    public ObservableField<String> senderAccount = new ObservableField<>("");
    public ObservableField<String> receiablesAccount = new ObservableField<>("");
    public ObservableField<String> nhAssetId = new ObservableField<>("");
    public ObservableField<String> minerFee = new ObservableField<>("");
    public ObservableField<String> squareHeight = new ObservableField<>("");
    public ObservableField<String> dealHash = new ObservableField<>("");
    public ObservableField<String> dealTime = new ObservableField<>("");
    public ObservableField<String> symbolType = new ObservableField<>(Utils.getString(R.string.module_asset_coin_type_test));


    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    public BindingCommand onReceiableCopyClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ClipData mClipData = ClipData.newPlainText("Label", receiablesAccount.get());
            ClipboardManagerInstance.getClipboardManager().setPrimaryClip(mClipData);
            ToastUtils.showShort(R.string.copy_success);
        }
    });

    public BindingCommand onTransferCopyClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ClipData mClipData = ClipData.newPlainText("Label", senderAccount.get());
            ClipboardManagerInstance.getClipboardManager().setPrimaryClip(mClipData);
            ToastUtils.showShort(R.string.copy_success);
        }
    });

    public void setDealDetailData(DealDetailModel dealDetailModel) {
        this.dealDetailModel = dealDetailModel;
        dealType.set(dealDetailModel.deal_type);
        senderAccount.set(dealDetailModel.from);
        receiablesAccount.set(dealDetailModel.to);
        nhAssetId.set(dealDetailModel.nh_asset_id);
        dealHash.set(dealDetailModel.tx_id);
        minerFee.set(dealDetailModel.fee + dealDetailModel.feeAssetSymbol);
        squareHeight.set(dealDetailModel.block_header);
        dealTime.set(dealDetailModel.time);
    }
}
