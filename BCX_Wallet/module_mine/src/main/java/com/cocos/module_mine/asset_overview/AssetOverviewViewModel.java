package com.cocos.module_mine.asset_overview;

import android.app.Application;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.global.SPKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.CurrencyUtils;
import com.cocos.library_base.utils.SPUtils;
import com.cocos.library_base.utils.Utils;


/**
 * @author ningkang.guo
 * @Date 2019/2/19
 */
public class AssetOverviewViewModel extends BaseViewModel {


    public AssetOverviewViewModel(@NonNull Application application) {
        super(application);
    }

    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public ObservableBoolean pSwitchObservable = new ObservableBoolean(false);
    }

    public ObservableField<String> totalAssetUnitType = new ObservableField<>(CurrencyUtils.getTotalCurrencyType());

    //总资产
    public ObservableField<String> totalAsset = new ObservableField<>(SPUtils.getString(Utils.getContext(), SPKeyGlobal.TOTAL_ASSET_VALUE, "0.00"));


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

}
