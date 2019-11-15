package com.cocos.module_mine.account_manage;

import android.app.Application;
import android.content.ClipData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.bcx_sdk.bcx_log.LogUtils;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.AssetBalanceModel;
import com.cocos.library_base.entity.AssetsModel;
import com.cocos.library_base.global.SPKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.SPUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.singleton.ClipboardManagerInstance;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_mine.R;

import java.math.BigDecimal;


/**
 * @author ningkang.guo
 * @Date 2019/2/20
 */
public class AccountManageViewModel extends BaseViewModel {

    public BigDecimal totalAssets = BigDecimal.ZERO;

    public ObservableField<String> symbolType = new ObservableField<>("");

    String accountNameStr;

    public AccountManageViewModel(@NonNull Application application) {
        super(application);
        String netType = SPUtils.getString(Utils.getContext(), SPKeyGlobal.NET_TYPE, "");
        symbolType.set(TextUtils.equals(netType, "0") ? Utils.getString(R.string.module_asset_coin_type_test) : "");
    }

    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public void setAccountName(String account) {
        this.accountNameStr = account;
        accountName.set(accountNameStr);
    }

    public class UIChangeObservable {
        public ObservableBoolean logOutObservable = new ObservableBoolean(false);
        public ObservableBoolean exportKeyObservable = new ObservableBoolean(false);
    }

    //返回按钮的点击事件
    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    //账户资产的绑定
    public ObservableField<String> totalAsset = new ObservableField<>("0.00");

    public ObservableField<String> assetSymbol = new ObservableField<>("COCOS");

    public ObservableField<String> accountName = new ObservableField<>("");

    //资产公钥的绑定
    public ObservableField<String> assetPublicKey = new ObservableField<>();

    //账户公钥的绑定
    public ObservableField<String> accountPublicKey = new ObservableField<>();

    //复制按钮的点击事件
    public BindingCommand assetPublicKeyCopyOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (TextUtils.isEmpty(assetPublicKey.get())) {
                return;
            }
            ClipData mClipData = ClipData.newPlainText("Label", assetPublicKey.get());
            ClipboardManagerInstance.getClipboardManager().setPrimaryClip(mClipData);
            ToastUtils.showShort(R.string.copy_success);
        }
    });

    //复制按钮的点击事件
    public BindingCommand accountPublicKeyCopyOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (TextUtils.isEmpty(accountPublicKey.get())) {
                return;
            }
            ClipData mClipData = ClipData.newPlainText("Label", accountPublicKey.get());
            ClipboardManagerInstance.getClipboardManager().setPrimaryClip(mClipData);
            ToastUtils.showShort(R.string.copy_success);
        }
    });

    //修改密码按钮的点击事件
    public BindingCommand modifyPasswordOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_MODIFY_PASSWORD).navigation();
        }
    });

    //导出私钥按钮的点击事件
    public BindingCommand exportPrivateKeyOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.exportKeyObservable.set(!uc.exportKeyObservable.get());
        }
    });

    //退出登录按钮的点击事件
    public BindingCommand loginOutOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.logOutObservable.set(!uc.logOutObservable.get());
        }
    });

    public void requestAccountManagerData(String accountId) {
        CocosBcxApiWrapper.getBcxInstance().get_account_balances(accountId, "1.3.0", new IBcxCallBack() {
            @Override
            public void onReceiveValue(final String s) {
                MainHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.d("get_account_balances", s);
                        AssetBalanceModel balanceEntity = GsonSingleInstance.getGsonInstance().fromJson(s, AssetBalanceModel.class);
                        if (!balanceEntity.isSuccess()) {
                            return;
                        }
                        final AssetBalanceModel.DataBean dataBean = balanceEntity.data;
                        //todo 价值计算
                        CocosBcxApiWrapper.getBcxInstance().lookup_asset_symbols(dataBean.asset_id, new IBcxCallBack() {
                            @Override
                            public void onReceiveValue(final String s) {
                                LogUtils.d("lookup_asset_symbols", s);
                                MainHandler.getInstance().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        AssetsModel assetModel = GsonSingleInstance.getGsonInstance().fromJson(s, AssetsModel.class);
                                        if (assetModel.isSuccess()) {
                                            //todo 价值计算
                                            totalAssets = totalAssets.add(dataBean.amount);
                                            totalAsset.set(String.valueOf(totalAssets));
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

        String activeKey = AccountHelperUtils.getActivePublicKey(accountNameStr);
        String ownerKey = AccountHelperUtils.getOwnerPublicKey(accountNameStr);
        assetPublicKey.set(ownerKey);
        accountPublicKey.set(activeKey);
    }
}
