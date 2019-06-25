package com.cocos.module_asset.ui.receivables;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.entity.AssetsModel;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.DecimalDigitsInputFilter;
import com.cocos.library_base.utils.NumberUtil;
import com.cocos.library_base.utils.StatusBarUtils;
import com.cocos.module_asset.BR;
import com.cocos.module_asset.R;
import com.cocos.module_asset.databinding.ActivityReceivablesBinding;

/**
 * @author ningkang.guo
 * @Date 2019/2/18
 */
@Route(path = RouterActivityPath.ACTIVITY_RECEIVABLES)
public class ReceivablesActivity extends BaseActivity<ActivityReceivablesBinding, ReceivablesViewModel> {

    private AssetsModel.AssetModel assetModel;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_receivables;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        try {
            StatusBarUtils.with(ReceivablesActivity.this).init();
            assetModel = (AssetsModel.AssetModel) getIntent().getExtras().getSerializable(IntentKeyGlobal.ASSET_MODEL);
        } catch (Exception e) {
        }
    }

    @Override
    public void initData() {
        viewModel.setAssetModel(assetModel);
        binding.etAmount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(assetModel.precision)});
        NumberUtil.setPricePoint1(binding.etAmount);
    }

    @Override
    public void onHandleEvent(EventBusCarrier busCarrier) {
        if (TextUtils.equals(EventTypeGlobal.RECEIVE_QRCODE_CHANGE, busCarrier.getEventType())) {
            binding.ivQrCode.setImageDrawable(viewModel.qrCodeDrawable);
        }
    }
}
