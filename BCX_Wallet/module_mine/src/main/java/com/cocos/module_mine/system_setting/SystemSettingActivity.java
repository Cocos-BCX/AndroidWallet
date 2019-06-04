package com.cocos.module_mine.system_setting;

import android.app.Application;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.entity.BaseResult;
import com.cocos.library_base.entity.NodeInfoModel;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.global.SPKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.ActivityContainer;
import com.cocos.library_base.utils.SPUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.multi_language.LocalManageUtil;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.module_mine.BR;
import com.cocos.module_mine.R;
import com.cocos.module_mine.databinding.ActivitySystemSettingBinding;
import com.cocos.module_mine.databinding.DialogMultiLanguageSelectBinding;
import com.cocos.module_mine.databinding.DialogMultiNodeWorkBinding;
import com.cocos.module_mine.multi_language.MultiLanguageViewModel;
import com.cocos.module_mine.multi_node_work.NodeWorkViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ningkang.guo
 * @Date 2019/3/1
 */
@Route(path = RouterActivityPath.ACTIVITY_SYSTEM_SETTING)
public class SystemSettingActivity extends BaseActivity<ActivitySystemSettingBinding, SystemSettingViewModel> {

    private BottomSheetDialog languageDialog;
    private BottomSheetDialog nodeNetDialog;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_system_setting;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }


    @Override
    public void onHandleEvent(EventBusCarrier busCarrier) {
        try {
            if (TextUtils.equals(EventTypeGlobal.DIALOG_DISMISS_TYPE, busCarrier.getEventType())) {
                languageDialog.dismiss();
                nodeNetDialog.dismiss();
                viewModel.languageType.set(LocalManageUtil.getSetLanguageString(Utils.getContext()));
            } else if (TextUtils.equals(EventTypeGlobal.SWITCH_LANGUAGE, busCarrier.getEventType())) {
                languageDialog.dismiss();
                viewModel.languageType.set(LocalManageUtil.getSetLanguageString(Utils.getContext()));
                ActivityContainer.finishAllActivity();
                ARouter.getInstance().build(RouterActivityPath.ACTIVITY_MAIN_PATH).navigation();
            } else if (TextUtils.equals(EventTypeGlobal.SWITCH_NODE_WORK, busCarrier.getEventType())) {
                NodeInfoModel.DataBean dataBean = (NodeInfoModel.DataBean) busCarrier.getObject();
                viewModel.netType.set(TextUtils.equals("0", dataBean.type) ? Utils.getString(R.string.module_mine_net_test_text) : Utils.getString(R.string.module_mine_net_main_text));
                reconnect(dataBean);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 切换节点
     */
    private void reconnect(final NodeInfoModel.DataBean dataBean) {
        // 初始化bcx节点连接
        List<String> nodeUrls = new ArrayList<>();
        nodeUrls.add(dataBean.ws);
        nodeUrls.add(dataBean.ws);
        nodeUrls.add(dataBean.ws);
        CocosBcxApiWrapper.getBcxInstance().init(this, dataBean.chainId, nodeUrls, dataBean.faucetUrl, dataBean.coreAsset, true, new IBcxCallBack() {
            @Override
            public void onReceiveValue(String value) {
                BaseResult resultEntity = GsonSingleInstance.getGsonInstance().fromJson(value, BaseResult.class);
                NodeInfoModel.DataBean selectedNodeModel = SPUtils.getObject(Utils.getContext(), SPKeyGlobal.NODE_WORK_MODEL_SELECTED);
                if (!resultEntity.isSuccess()) {
                    init(selectedNodeModel);
                    nodeNetDialog.dismiss();
                    return;
                }
                nodeNetDialog.dismiss();
                SPUtils.putObject(Utils.getContext(), SPKeyGlobal.NODE_WORK_MODEL_SELECTED, dataBean);
                List<String> accountNames = CocosBcxApiWrapper.getBcxInstance().get_dao_account_names();
                if (accountNames.size() <= 0) {
                    ARouter.getInstance().build(RouterActivityPath.ACTIVITY_PASSWORD_LOGIN).navigation();
                    ActivityContainer.finishAllActivity();
                } else {
                    AccountHelperUtils.setCurrentAccountName(accountNames.get(0));
                }
            }
        });
    }


    /**
     * 切换节点
     */
    private void init(final NodeInfoModel.DataBean dataBean) {
        // 初始化bcx节点连接
        List<String> nodeUrls = new ArrayList<>();
        nodeUrls.add(dataBean.ws);
        nodeUrls.add(dataBean.ws);
        nodeUrls.add(dataBean.ws);
        CocosBcxApiWrapper.getBcxInstance().init(this, dataBean.chainId, nodeUrls, dataBean.faucetUrl, dataBean.coreAsset, true, new IBcxCallBack() {
            @Override
            public void onReceiveValue(String value) {
                BaseResult resultEntity = GsonSingleInstance.getGsonInstance().fromJson(value, BaseResult.class);
                if (!resultEntity.isSuccess()) {
                    return;
                }
                nodeNetDialog.dismiss();
                SPUtils.putObject(Utils.getContext(), SPKeyGlobal.NODE_WORK_MODEL_SELECTED, dataBean);
                List<String> accountNames = CocosBcxApiWrapper.getBcxInstance().get_dao_account_names();
                if (accountNames.size() <= 0) {
                    ARouter.getInstance().build(RouterActivityPath.ACTIVITY_PASSWORD_LOGIN).navigation();
                    ActivityContainer.finishAllActivity();
                } else {
                    AccountHelperUtils.setCurrentAccountName(accountNames.get(0));
                }
            }
        });
    }

    @Override
    public void initViewObservable() {

        // 语言切换弹窗
        languageDialog = new BottomSheetDialog(this);
        DialogMultiLanguageSelectBinding languageBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_multi_language_select, null, false);
        languageDialog.setContentView(languageBinding.getRoot());
        languageBinding.setViewModel(new MultiLanguageViewModel((Application) Utils.getContext()));
        viewModel.uc.multiLanguageObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                languageDialog.show();
            }
        });

        // 节点切换弹窗
        nodeNetDialog = new BottomSheetDialog(this);
        DialogMultiNodeWorkBinding nodeNetBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_multi_node_work, null, false);
        nodeNetDialog.setContentView(nodeNetBinding.getRoot());
        final NodeWorkViewModel nodeWorkViewModel = new NodeWorkViewModel((Application) Utils.getContext());
        nodeNetBinding.setViewModel(nodeWorkViewModel);
        viewModel.uc.netSettingObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                nodeWorkViewModel.requestNodeListData();
                nodeNetDialog.show();
            }
        });
    }

}
