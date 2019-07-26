package com.cocos.module_mine.asset_operate.sale_nhasset;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.entity.OperateResultModel;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_mine.R;
import com.cocos.module_mine.entity.SaleNHAssetParamsModel;

import org.greenrobot.eventbus.EventBus;

/**
 * @author ningkang.guo
 * @Date 2019/2/20
 */
public class SaleNhConfirmViewModel extends BaseViewModel {


    public SaleNhConfirmViewModel(@NonNull Application application) {
        super(application);
    }

    private SaleNHAssetParamsModel saleAssetParamsModel;
    public ObservableField<String> nhAssetId = new ObservableField<>("");
    public ObservableField<String> priceAmount = new ObservableField<>("");
    public ObservableField<String> validTime = new ObservableField<>("");
    public ObservableField<String> minerFee = new ObservableField<>("");
    public ObservableField<String> orderMemo = new ObservableField<>("");

    // 关闭弹窗
    public BindingCommand dismissOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.DIALOG_DISMISS_TYPE);
            eventBusCarrier.setObject(null);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });

    //出售按钮的点击事件
    public BindingCommand saleConfirmOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            CocosBcxApiWrapper.getBcxInstance().create_nh_asset_order("otcaccount", AccountHelperUtils.getCurrentAccountName(),
                    saleAssetParamsModel.getPassword(), saleAssetParamsModel.getNhAssetId(), saleAssetParamsModel.getSaleFee(),
                    "COCOS", saleAssetParamsModel.getOrderMemo(), saleAssetParamsModel.getPriceAmount(),
                    saleAssetParamsModel.getPriceSymbol(), Long.parseLong(saleAssetParamsModel.getValidTime()), new IBcxCallBack() {
                        @Override
                        public void onReceiveValue(final String s) {
                            MainHandler.getInstance().post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        final OperateResultModel feeModel = GsonSingleInstance.getGsonInstance().fromJson(s, OperateResultModel.class);
                                        if (feeModel.code == 105) {
                                            ToastUtils.showShort(R.string.module_mine_wrong_password);
                                            return;
                                        }

                                        if (!feeModel.isSuccess()) {
                                            ToastUtils.showShort(R.string.net_work_failed);
                                            return;
                                        }
                                        ToastUtils.showShort(R.string.module_mine_sale_nh_success);
                                        EventBusCarrier eventBusCarrier = new EventBusCarrier();
                                        eventBusCarrier.setEventType(EventTypeGlobal.SALE_SUCCESS);
                                        eventBusCarrier.setObject(null);
                                        EventBus.getDefault().post(eventBusCarrier);
                                    } catch (Exception e) {
                                        ToastUtils.showShort(R.string.net_work_failed);
                                    }
                                }
                            });
                        }
                    });
        }
    });


    public void setSaleInfoData(SaleNHAssetParamsModel saleNHAssetParamsModel) {
        this.saleAssetParamsModel = saleNHAssetParamsModel;
        nhAssetId.set(saleNHAssetParamsModel.getNhAssetId());
        priceAmount.set(saleNHAssetParamsModel.getPriceAmount() + " " + saleNHAssetParamsModel.getPriceSymbol());
        validTime.set(saleNHAssetParamsModel.getValidTime() + " s");
        minerFee.set(saleNHAssetParamsModel.getMinerFee() + " COCOS");
        orderMemo.set(saleNHAssetParamsModel.getOrderMemo());
    }
}
