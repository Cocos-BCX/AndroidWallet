package com.cocos.module_mine.account_manage;

import android.databinding.Observable;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.entity.DaoAccountEntity;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.module_mine.BR;
import com.cocos.module_mine.R;
import com.cocos.module_mine.databinding.ActivityAccountManageListBinding;

import java.util.List;

/**
 * @author ningkang.guo
 * @Date 2019/4/8
 */

@Route(path = RouterActivityPath.ACTIVITY_ACCOUNT_MANAGE_LIST)
public class AccountManagerListActivity extends BaseActivity<ActivityAccountManageListBinding, AccountManagerListViewModel> {
    @Override
    public int initContentView(Bundle bundle) {
        return R.layout.activity_account_manage_list;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        loadData();
    }

    private void loadData() {
        try {
            CocosBcxApiWrapper.getBcxInstance().queryAllAccountByChainId(new IBcxCallBack() {
                @Override
                public void onReceiveValue(String s) {
                    DaoAccountEntity daoAccountEntity = GsonSingleInstance.getGsonInstance().fromJson(s, DaoAccountEntity.class);
                    if (daoAccountEntity.isSuccess()) {
                        viewModel.requestAccountsListData(daoAccountEntity.data);
                    } else {
                        viewModel.requestAccountsListData(null);
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.uc.loginViewObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                ARouter.getInstance().build(RouterActivityPath.ACTIVITY_PASSWORD_LOGIN).navigation();
                finish();
            }
        });
    }
}
