package com.cocos.module_mine.asset_overview;

import android.app.Application;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.bcx_sdk.bcx_log.LogUtils;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.AllAssetBalanceModel;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;

import java.math.BigDecimal;


/**
 * @author ningkang.guo
 * @Date 2019/2/19
 */
public class AssetOverviewViewModel extends BaseViewModel {


    public AssetOverviewViewModel(@NonNull Application application) {
        super(application);
    }

    public BigDecimal totalAssets = BigDecimal.ZERO;

    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public ObservableBoolean pSwitchObservable = new ObservableBoolean(false);
    }

    //总资产
    public ObservableField<String> totalAsset = new ObservableField<>("0.00");


    //消息中心按钮的点击事件
    public BindingCommand OrderManageCentersItemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_ORDER_MANAGE).navigation();
        }
    });

    //返回按钮的点击事件
    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    //眼睛按钮的点击事件
    public BindingCommand assetVisibleOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //让观察者的数据改变,逻辑从ViewModel层转到View层，在View层的监听则会被调用
            uc.pSwitchObservable.set(!uc.pSwitchObservable.get());
        }
    });

    public void requestAssetsListData(String accountId) {
        CocosBcxApiWrapper.getBcxInstance().get_all_account_balances(accountId, new IBcxCallBack() {
            @Override
            public void onReceiveValue(final String s) {
                MainHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.d("get_all_account_balances", s);
                        AllAssetBalanceModel balanceEntity = GsonSingleInstance.getGsonInstance().fromJson(s, AllAssetBalanceModel.class);
                        if (balanceEntity.isSuccess() && balanceEntity.getData().size() > 0) {
                            for (final AllAssetBalanceModel.DataBean dataBean : balanceEntity.getData()) {
                                //todo 价值计算
                                totalAssets = totalAssets.add(dataBean.getAmount().multiply(BigDecimal.ZERO).setScale(2));
                                totalAsset.set(String.valueOf(totalAssets));
                            }
                        }
                    }
                });
            }
        });
    }

}
