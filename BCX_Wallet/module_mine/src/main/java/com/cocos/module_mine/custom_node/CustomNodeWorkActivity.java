package com.cocos.module_mine.custom_node;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.module_mine.BR;
import com.cocos.module_mine.R;
import com.cocos.module_mine.databinding.ActivityCustomNodeBinding;

/**
 * @author ningkang.guo
 * @Date 2019/6/3
 */
@Route(path = RouterActivityPath.ACTIVITY_CUSTOM_NODE_WORK)
public class CustomNodeWorkActivity extends BaseActivity<ActivityCustomNodeBinding, CustomNodeWorkViewModel> {
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_custom_node;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}
