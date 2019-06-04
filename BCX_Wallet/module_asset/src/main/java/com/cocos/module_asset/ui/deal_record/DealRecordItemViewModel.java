package com.cocos.module_asset.ui.deal_record;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.library_base.base.ItemViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.AssetsModel;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_asset.R;
import com.cocos.module_asset.entity.BlockHeaderModel;
import com.cocos.module_asset.entity.DealDetailModel;
import com.cocos.module_asset.entity.DealRecordModel;

import java.math.BigDecimal;

/**
 * @author ningkang.guo
 * @Date 2019/2/20
 */
public class DealRecordItemViewModel extends ItemViewModel<DealRecordViewModel> {


    public ObservableField<String> account = new ObservableField<>();
    public ObservableField<String> operationAmount = new ObservableField<>();
    public ObservableField<String> operationDate = new ObservableField<>();
    public ObservableField<String> symbolType = new ObservableField<>(Utils.getString(R.string.module_asset_coin_type_test));

    public ObservableInt operationAmountColor = new ObservableInt(Utils.getColor(R.color.color_4868DC));
    public Drawable drawableImg;
    public DealRecordModel.DealRecordItemModel dealRecordModel;
    DealDetailModel dealDetailModel = new DealDetailModel();

    public DealRecordItemViewModel(@NonNull final DealRecordViewModel viewModel, DealRecordModel.DealRecordItemModel dealRecordModel) {
        super(viewModel);
        this.dealRecordModel = dealRecordModel;
        final Object opObject = dealRecordModel.op.get(1);
        final DealRecordModel.OpBean opBean = GsonSingleInstance.getGsonInstance().fromJson(GsonSingleInstance.getGsonInstance().toJson(opObject), DealRecordModel.OpBean.class);
        // 转账
        final String fromAccountName = CocosBcxApiWrapper.getBcxInstance().get_account_name_by_id(opBean.from);
        final String toAccountName = CocosBcxApiWrapper.getBcxInstance().get_account_name_by_id(opBean.to);
        dealDetailModel.from = fromAccountName;
        dealDetailModel.to = toAccountName;
        //当前账户不是收款账户则为转账
        final boolean isTransferAccount = !TextUtils.equals(AccountHelperUtils.getCurrentAccountName(), toAccountName);
        drawableImg = Utils.getDrawable(isTransferAccount ? R.drawable.deal_record_transfer_operation_icon : R.drawable.deal_record_receive_operation_icon);
        if (isTransferAccount) {
            account.set(toAccountName);
            operationAmountColor.set(Utils.getColor(R.color.color_4868DC));
            dealDetailModel.deal_type = Utils.getString(R.string.module_asset_transfer_title);
        } else {
            account.set(fromAccountName);
            operationAmountColor.set(Utils.getColor(R.color.color_2FC49F));
            dealDetailModel.deal_type = Utils.getString(R.string.module_asset_receivables_title);
        }
        if (null != opBean.memo) {
            dealDetailModel.memo = opBean.memo;
        }
        dealDetailModel.block_header = String.valueOf(dealRecordModel.block_num);

        CocosBcxApiWrapper.getBcxInstance().lookup_asset_symbols(opBean.amount.asset_id, new IBcxCallBack() {
            @Override
            public void onReceiveValue(final String assets) {
                final AssetsModel assetModel = GsonSingleInstance.getGsonInstance().fromJson(assets, AssetsModel.class);
                MainHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        if (!assetModel.isSuccess()) {
                            return;
                        }
                        // precision
                        BigDecimal ratio = new BigDecimal(Math.pow(10, assetModel.getData().precision));
                        String dealAmount = String.valueOf(opBean.amount.amount.divide(ratio).add(BigDecimal.ZERO)) + assetModel.getData().symbol;
                        operationAmount.set(isTransferAccount ? "-" + dealAmount : "+" + dealAmount);
                        dealDetailModel.amount = String.valueOf(opBean.amount.amount.divide(ratio).add(BigDecimal.ZERO));
                        dealDetailModel.amountAssetSymbol = assetModel.getData().symbol;
                    }
                });
            }
        });

        CocosBcxApiWrapper.getBcxInstance().lookup_asset_symbols(opBean.fee.asset_id, new IBcxCallBack() {
            @Override
            public void onReceiveValue(final String assets) {
                final AssetsModel assetModel = GsonSingleInstance.getGsonInstance().fromJson(assets, AssetsModel.class);
                MainHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        if (!assetModel.isSuccess()) {
                            return;
                        }
                        // precision
                        BigDecimal ratio = new BigDecimal(Math.pow(10, assetModel.getData().precision));
                        dealDetailModel.fee = String.valueOf(opBean.fee.amount.divide(ratio).add(BigDecimal.ZERO));
                        dealDetailModel.feeAssetSymbol = assetModel.getData().symbol;
                    }
                });
            }
        });

        CocosBcxApiWrapper.getBcxInstance().get_block_header(dealRecordModel.block_num, new IBcxCallBack() {
            @Override
            public void onReceiveValue(final String blockHeader) {
                MainHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        final BlockHeaderModel assetModel = GsonSingleInstance.getGsonInstance().fromJson(blockHeader, BlockHeaderModel.class);
                        if (!assetModel.isSuccess()) {
                            return;
                        }
                        String timestamp = assetModel.data.timestamp;
                        String[] times = timestamp.split("T");
                        //时间
                        String[] hours = times[1].split(":", 2);
                        int hour = Integer.parseInt(hours[0]) + 8;
                        String time = times[0].replace("-", ".") + "  " + hour + ":" + hours[1];
                        operationDate.set(time);
                        dealDetailModel.time = time;
                    }
                });
            }
        });

    }

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKeyGlobal.DEAL_DETAIL_MODEL, dealDetailModel);
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_RECORD_DETAIL).with(bundle).navigation();
        }
    });
}
