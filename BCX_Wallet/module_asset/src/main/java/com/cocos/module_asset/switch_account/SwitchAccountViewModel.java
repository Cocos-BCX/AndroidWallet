package com.cocos.module_asset.switch_account;

import android.app.Application;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.global.SPKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.SPUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.module_asset.BR;
import com.cocos.module_asset.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @author ningkang.guo
 * @Date 2019/2/15
 */
public class SwitchAccountViewModel extends BaseViewModel {

    public SwitchAccountViewModel(@NonNull Application application) {
        super(application);
        String netType = SPUtils.getString(Utils.getContext(), SPKeyGlobal.NET_TYPE, "");
        symbolType.set(TextUtils.equals(netType, "0") ? Utils.getString(R.string.module_asset_coin_type_test) : "");
        List<String> accountNames = CocosBcxApiWrapper.getBcxInstance().get_dao_account_names();
        requestAccountListData(accountNames);
    }

    //当前代币名称
    public ObservableField<String> currentTokenName = new ObservableField<>("COCOS");

    public ObservableField<String> symbolType = new ObservableField<>("");

    public BindingCommand dissmissOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.DIALOG_DISMISS_TYPE);
            eventBusCarrier.setObject(null);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });


    //添加账号按钮
    public BindingCommand addAccountOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_CREATE_ACCOUNT).navigation();
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.DIALOG_DISMISS_TYPE);
            eventBusCarrier.setObject(null);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });


    public ObservableList<SwitchAccountItemViewModel> accountObservableList = new ObservableArrayList<>();
    public final BindingRecyclerViewAdapter<SwitchAccountItemViewModel> accountAdapter = new BindingRecyclerViewAdapter<>();
    public ItemBinding<SwitchAccountItemViewModel> accountItemBinding = ItemBinding.of(BR.viewModel, R.layout.module_asset_item_account);

    public void requestAccountListData(List<String> accountNames) {
        for (String accountName : accountNames) {
            SwitchAccountItemViewModel itemViewModel = new SwitchAccountItemViewModel(SwitchAccountViewModel.this, accountName);
            accountObservableList.add(itemViewModel);
        }
    }
}
