package com.cocos.module_mine.export_private;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.entity.PrivateKeyModel;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.module_mine.BR;
import com.cocos.module_mine.R;
import com.cocos.module_mine.databinding.ActivityKeyExportBinding;

/**
 * @author ningkang.guo
 * @Date 2019/2/22
 */

@Route(path = RouterActivityPath.ACTIVITY_KEY_EXPORT)
public class ExportKeyActivity extends BaseActivity<ActivityKeyExportBinding, ExportKeyViewModel> {

    private PrivateKeyModel privateKeyModel;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_key_export;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }


    @Override
    public void initParam() {
        try {
            privateKeyModel = (PrivateKeyModel) getIntent().getExtras().getSerializable(IntentKeyGlobal.KEY_MODEL);
        } catch (Exception e) {
        }
    }

    @Override
    public void initData() {
        viewModel.setPrivateKeyModel(privateKeyModel);
    }
}
