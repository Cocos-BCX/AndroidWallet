package com.cocos.module_mine.account_manage;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
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
        try {
            List<String> accountNames = AccountHelperUtils.getAccountNames();
            viewModel.requestAccountsListData(accountNames);
        } catch (Exception e) {

        }
    }
}
