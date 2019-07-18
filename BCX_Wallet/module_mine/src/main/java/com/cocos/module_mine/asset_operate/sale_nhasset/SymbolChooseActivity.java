package com.cocos.module_mine.asset_operate.sale_nhasset;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.library_base.BR;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.module_mine.R;
import com.cocos.module_mine.databinding.ActivitySymbolChooseBinding;

/**
 * @author ningkang.guo
 * @Date 2019/7/18
 */
@Route(path = RouterActivityPath.ACTIVITY_SYMBOL_LIST)
public class SymbolChooseActivity extends BaseActivity<ActivitySymbolChooseBinding, SymbolChooseViewModel> {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_symbol_choose;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }


    @Override
    public void initData() {
        viewModel.requestAllSymbols();
    }

    @Override
    public void onHandleEvent(EventBusCarrier busCarrier) {
        if (null != busCarrier && TextUtils.equals(EventTypeGlobal.SET_PRICE_SYMBOL, busCarrier.getEventType())) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(IntentKeyGlobal.PRICE_SYMBOL, (String) busCarrier.getObject());
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
            return;
        }
    }
}
