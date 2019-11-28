package com.cocos.module_asset.ui.deal_record;

import android.databinding.Observable;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.bcx_sdk.bcx_log.LogUtils;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.base.BaseVerifyPasswordDialog;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;
import com.cocos.module_asset.BR;
import com.cocos.module_asset.R;
import com.cocos.module_asset.databinding.ActivityRecordDetailBinding;
import com.cocos.module_asset.entity.DealDetailModel;
import com.cocos.module_asset.entity.MemoData;

/**
 * @author ningkang.guo
 * @Date 2019/2/20
 */
@Route(path = RouterActivityPath.ACTIVITY_RECORD_DETAIL)
public class RecordDetailActivity extends BaseActivity<ActivityRecordDetailBinding, RecordDetailViewModel> {

    private DealDetailModel dealDetailModel;

    @Override
    public int initContentView(Bundle bundle) {
        return R.layout.activity_record_detail;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        try {
            dealDetailModel = (DealDetailModel) getIntent().getExtras().getSerializable(IntentKeyGlobal.DEAL_DETAIL_MODEL);
        } catch (Exception e) {

        }
    }

    @Override
    public void initData() {
        viewModel.setDealDetailData(dealDetailModel);
    }


    @Override
    public void initViewObservable() {
        if (null != dealDetailModel.memo && (Double) dealDetailModel.memo.get(0) == 1) {
            viewModel.uc.checkMemoObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    final BaseVerifyPasswordDialog passwordVerifyDialog = new BaseVerifyPasswordDialog();
                    passwordVerifyDialog.show(getSupportFragmentManager(), "passwordVerifyDialog");
                    passwordVerifyDialog.setPasswordListener(new BaseVerifyPasswordDialog.IPasswordListener() {
                        @Override
                        public void onFinish(String password) {
                            CocosBcxApiWrapper.getBcxInstance().decrypt_memo_message(AccountHelperUtils.getCurrentAccountName(), password, GsonSingleInstance.getGsonInstance().toJson(dealDetailModel.memo.get(1)), new IBcxCallBack() {
                                @Override
                                public void onReceiveValue(final String memo) {
                                    LogUtils.d("decrypt_memo_message", memo);
                                    MainHandler.getInstance().post(new Runnable() {
                                        @Override
                                        public void run() {
                                            final MemoData memoData = GsonSingleInstance.getGsonInstance().fromJson(memo, MemoData.class);
                                            if (memoData.code == 105) {
                                                ToastUtils.showShort(R.string.module_asset_wrong_password);
                                                return;
                                            }
                                            if (!memoData.isSuccess()) {
                                                return;
                                            }
                                            viewModel.dealMemo.set(memoData.data);
                                        }
                                    });
                                }
                            });
                        }

                        @Override
                        public void cancel() {

                        }
                    });
                }
            });
        }
    }
}
