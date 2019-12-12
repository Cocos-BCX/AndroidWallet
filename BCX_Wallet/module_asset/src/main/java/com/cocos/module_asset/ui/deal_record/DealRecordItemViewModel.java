package com.cocos.module_asset.ui.deal_record;

import android.annotation.SuppressLint;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.bcx_sdk.bcx_error.ContractNotFoundException;
import com.cocos.bcx_sdk.bcx_error.NetworkStatusException;
import com.cocos.bcx_sdk.bcx_wallet.chain.contract_object;
import com.cocos.library_base.base.ItemViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.AssetsModel;
import com.cocos.library_base.entity.FeesModel;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.global.SPKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.SPUtils;
import com.cocos.library_base.utils.TimeUtil;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_asset.R;
import com.cocos.module_asset.entity.BlockHeaderModel;
import com.cocos.module_asset.entity.DealDetailModel;
import com.cocos.module_asset.entity.DealRecordModel;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ningkang.guo
 * @Date 2019/2/20
 */
public class DealRecordItemViewModel extends ItemViewModel<DealRecordViewModel> {


    public ObservableField<String> account = new ObservableField<>();
    public ObservableField<String> operationAmount = new ObservableField<>();
    public ObservableField<String> operationDate = new ObservableField<>();
    public ObservableField<String> symbolType = new ObservableField<>("");
    public ObservableInt symbolTypeVisible = new ObservableInt(View.GONE);

    public ObservableInt operationAmountColor = new ObservableInt(Utils.getColor(R.color.color_4868DC));
    public Drawable drawableImg;
    public DealRecordModel.DealRecordItemModel dealRecordModel;
    DealDetailModel dealDetailModel = new DealDetailModel();

    /**
     * @param viewModel
     * @param dealRecordModel
     */
    public DealRecordItemViewModel(@NonNull final DealRecordViewModel viewModel, DealRecordModel.DealRecordItemModel dealRecordModel) {
        super(viewModel);
        String netType = SPUtils.getString(Utils.getContext(), SPKeyGlobal.NET_TYPE, "");
        symbolType.set(TextUtils.equals(netType, "0") ? Utils.getString(R.string.module_asset_coin_type_test) : "");
        this.dealRecordModel = dealRecordModel;
        double option = (double) dealRecordModel.op.get(0);
        dealDetailModel.option = option;
        if (0 == option) {
            try {
                symbolTypeVisible.set(View.VISIBLE);
                final Object opObject = dealRecordModel.op.get(1);
                final DealRecordModel.OpBean opBean = GsonSingleInstance.getGsonInstance().fromJson(GsonSingleInstance.getGsonInstance().toJson(opObject), DealRecordModel.OpBean.class);
                // 转账
                final String fromAccountName = CocosBcxApiWrapper.getBcxInstance().get_account_name_by_id_sync(opBean.from);
                final String toAccountName = CocosBcxApiWrapper.getBcxInstance().get_account_name_by_id_sync(opBean.to);
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

                // 转账币种查询
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
                                String dealAmount = opBean.amount.amount.divide(ratio).add(BigDecimal.ZERO) + assetModel.getData().symbol;
                                operationAmount.set(isTransferAccount ? "-" + dealAmount : "+" + dealAmount);
                                dealDetailModel.amount = dealAmount;
                            }
                        });
                    }
                });
                final Object resultObject = dealRecordModel.result.get(1);
                final FeesModel feesModel = GsonSingleInstance.getGsonInstance().fromJson(GsonSingleInstance.getGsonInstance().toJson(resultObject), FeesModel.class);
                FeesModel.FeesBean feesBean = feesModel.fees.get(0);
                dealDetailModel.fee = String.valueOf(TextUtils.equals("1.3.0", feesBean.asset_id) ? feesModel.fees.get(0).amount.divide(BigDecimal.valueOf(Math.pow(10, 5))) : 0);
                dealDetailModel.feeSymbol = "COCOS";
            } catch (Exception e) {
                ToastUtils.showShort(R.string.net_work_failed);
            }
        } else if (35 == option) {
            try {
                drawableImg = Utils.getDrawable(R.drawable.deal_record_contract_icon);
                operationAmountColor.set(Utils.getColor(R.color.color_4B4BD9));
                symbolTypeVisible.set(View.GONE);
                dealDetailModel.deal_type = Utils.getString(R.string.module_asset_contract_type);
                final Object opObject = dealRecordModel.op.get(1);
                final DealRecordModel.ContractOp contractOp = GsonSingleInstance.getGsonInstance().fromJson(GsonSingleInstance.getGsonInstance().toJson(opObject), DealRecordModel.ContractOp.class);
                String caller = CocosBcxApiWrapper.getBcxInstance().get_account_name_by_id_sync(contractOp.caller);
                account.set(caller);
                dealDetailModel.caller = caller;
                contract_object contract_object = CocosBcxApiWrapper.getBcxInstance().get_contract_object(contractOp.contract_id);
                operationAmount.set(contract_object.name);
                dealDetailModel.contract_name = contract_object.name;
                dealDetailModel.function_name = contractOp.function_name;
                List<List<Object>> contract_ABI = contract_object.contract_ABI;
                for (List<Object> objects : contract_ABI) {
                    for (int i = 0; i < objects.size(); i++) {
                        // 获取方法名
                        LinkedTreeMap<String, ArrayList> o = (LinkedTreeMap<String, ArrayList>) objects.get(0);
                        ArrayList<Object> objects1 = o.get("key");
                        LinkedTreeMap<String, String> linkedTreeMap = (LinkedTreeMap<String, String>) objects1.get(1);
                        String functionName = linkedTreeMap.get("v");
                        if (TextUtils.equals(contractOp.function_name, functionName)) {
                            JsonObject jsonObject = new JsonObject();
                            // 获取方法参数
                            ArrayList<Object> objects2 = (ArrayList<Object>) objects.get(1);
                            LinkedTreeMap<String, Object> linkedTreeMap1 = (LinkedTreeMap<String, Object>) objects2.get(1);
                            ArrayList<String> args = (ArrayList<String>) linkedTreeMap1.get("arglist");
                            if (null != args && args.size() > 0) {
                                for (int j = 0; j < contractOp.value_list.size(); j++) {
                                    List<Object> value_list = contractOp.value_list.get(j);
                                    LinkedTreeMap<Object, Object> linkedTreeMap2 = (LinkedTreeMap<Object, Object>) value_list.get(1);
                                    Object argValue = linkedTreeMap2.get("v");
                                    jsonObject.addProperty(args.get(j), argValue.toString());
                                    dealDetailModel.params = jsonObject.toString();
                                }
                            }
                        }
                    }
                }
                final Object resultObject = dealRecordModel.result.get(1);
                final FeesModel feesModel = GsonSingleInstance.getGsonInstance().fromJson(GsonSingleInstance.getGsonInstance().toJson(resultObject), FeesModel.class);
                FeesModel.FeesBean feesBean = feesModel.fees.get(0);
                dealDetailModel.fee = String.valueOf(TextUtils.equals("1.3.0", feesBean.asset_id) ? feesModel.fees.get(0).amount.divide(BigDecimal.valueOf(Math.pow(10, 5))) : 0);
                dealDetailModel.feeSymbol = "COCOS";
            } catch (ContractNotFoundException e) {
                ToastUtils.showShort(R.string.net_work_failed);
            } catch (NetworkStatusException e) {
                ToastUtils.showShort(R.string.net_work_failed);
            } catch (ClassCastException e) {
                ToastUtils.showShort(R.string.net_work_failed);
            } catch (Exception e) {
                ToastUtils.showShort(R.string.net_work_failed);
            }
        } else if (42 == option) {
            symbolTypeVisible.set(View.GONE);
            final Object opObject = dealRecordModel.op.get(1);
            final DealRecordModel.OpBean opBean = GsonSingleInstance.getGsonInstance().fromJson(GsonSingleInstance.getGsonInstance().toJson(opObject), DealRecordModel.OpBean.class);
            // 转账
            final String fromAccountName = CocosBcxApiWrapper.getBcxInstance().get_account_name_by_id_sync(opBean.from);
            final String toAccountName = CocosBcxApiWrapper.getBcxInstance().get_account_name_by_id_sync(opBean.to);
            dealDetailModel.from = fromAccountName;
            dealDetailModel.to = toAccountName;
            //当前账户不是收款账户则为转账
            final boolean isTransferAccount = !TextUtils.equals(AccountHelperUtils.getCurrentAccountName(), toAccountName);
            drawableImg = Utils.getDrawable(isTransferAccount ? R.drawable.deal_record_nh_asset_transfer : R.drawable.deal_record_nh_asset_receive);
            if (isTransferAccount) {
                account.set(toAccountName);
                operationAmountColor.set(Utils.getColor(R.color.color_4868DC));
            } else {
                account.set(fromAccountName);
                operationAmountColor.set(Utils.getColor(R.color.color_2FC49F));
            }
            operationAmount.set(opBean.nh_asset + (TextUtils.equals(netType, "0") ? Utils.getString(R.string.module_asset_coin_type_test) : ""));
            dealDetailModel.deal_type = Utils.getString(R.string.module_asset_transfer_nh_title);
            dealDetailModel.nh_asset_id = opBean.nh_asset;
            final Object resultObject = dealRecordModel.result.get(1);
            final FeesModel feesModel = GsonSingleInstance.getGsonInstance().fromJson(GsonSingleInstance.getGsonInstance().toJson(resultObject), FeesModel.class);
            FeesModel.FeesBean feesBean = feesModel.fees.get(0);
            dealDetailModel.fee = String.valueOf(TextUtils.equals("1.3.0", feesBean.asset_id) ? feesModel.fees.get(0).amount.divide(BigDecimal.valueOf(Math.pow(10, 5))) : 0);
            dealDetailModel.feeSymbol = "COCOS";
        }

        dealDetailModel.block_header = String.valueOf(dealRecordModel.block_num);
        dealDetailModel.tx_id = dealRecordModel.id;

        // 转账区块信息/时间查询
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
                        if (!TextUtils.isEmpty(timestamp)) {
                            String pattern = "yyyy-MM-dd'T'HH:mm:ss";
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
                            Date dateObject = null;
                            try {
                                dateObject = sDateFormat.parse(timestamp);
                                String time = TimeUtil.formDate(dateObject);
                                operationDate.set(time);
                                dealDetailModel.time = time;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
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
            if (dealDetailModel.option == 0) {
                ARouter.getInstance().build(RouterActivityPath.ACTIVITY_RECORD_DETAIL).with(bundle).navigation();
            } else if (dealDetailModel.option == 35) {
                ARouter.getInstance().build(RouterActivityPath.ACTIVITY_CONTRACT_RECORD_DETAIL).with(bundle).navigation();
            } else if (dealDetailModel.option == 42) {
                ARouter.getInstance().build(RouterActivityPath.ACTIVITY_NH_TRANSFER_RECORD_DETAIL).with(bundle).navigation();
            }
        }
    });
}
