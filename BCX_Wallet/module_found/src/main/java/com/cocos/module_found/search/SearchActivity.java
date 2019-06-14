package com.cocos.module_found.search;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.module_found.BR;
import com.cocos.module_found.R;
import com.cocos.module_found.databinding.ActivitySearchBinding;


/**
 * @author ningkang.guo
 * @Date 2019/6/14
 */
@Route(path = RouterActivityPath.ACTIVITY_SEARCH)
public class SearchActivity extends BaseActivity<ActivitySearchBinding, SearchViewModel> {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_search;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}
