package com.cocos.module_mine.custom_node;

import android.app.Application;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.NodeInfoModel;
import com.cocos.library_base.global.SPKeyGlobal;
import com.cocos.library_base.utils.SPUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.module_mine.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ningkang.guo
 * @Date 2019/6/3
 */
public class CustomNodeWorkViewModel extends BaseViewModel {

    public CustomNodeWorkViewModel(@NonNull Application application) {
        super(application);
    }

    public ObservableField<String> nodeUrl = new ObservableField<>();
    public ObservableField<String> chainId = new ObservableField<>();
    public ObservableField<String> faucetUrl = new ObservableField<>();
    public ObservableField<String> coreAsset = new ObservableField<>();
    public ObservableField<String> nodeName = new ObservableField<>();

    public ObservableBoolean testNodeCheck = new ObservableBoolean(true);
    public ObservableBoolean mainNodeCheck = new ObservableBoolean(false);

    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    public BindingCommand testNodeCheckCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            testNodeCheck.set(true);
            mainNodeCheck.set(false);
        }
    });

    public BindingCommand mainNodeCheckCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            testNodeCheck.set(false);
            mainNodeCheck.set(true);
        }
    });

    public BindingCommand complishOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (TextUtils.isEmpty(nodeUrl.get())) {
                ToastUtils.showShort(R.string.module_mine_node_url_hint);
                return;
            }
            if (TextUtils.isEmpty(chainId.get())) {
                ToastUtils.showShort(R.string.module_mine_custom_chain_id_hint);
                return;
            }
            if (TextUtils.isEmpty(faucetUrl.get())) {
                ToastUtils.showShort(R.string.module_mine_custom_faucet_url_hint);
                return;
            }
            if (TextUtils.isEmpty(coreAsset.get())) {
                ToastUtils.showShort(R.string.module_mine_custom_core_asset_hint);
                return;
            }
            if (TextUtils.isEmpty(nodeName.get())) {
                ToastUtils.showShort(R.string.module_mine_custom_node_name_hint);
                return;
            }
            NodeInfoModel.DataBean dataBean = new NodeInfoModel.DataBean();
            dataBean.type = testNodeCheck.get() ? "0" : "1";
            dataBean.coreAsset = coreAsset.get();
            dataBean.name = nodeName.get();
            dataBean.ws = nodeUrl.get();
            dataBean.chainId = chainId.get();
            dataBean.faucetUrl = faucetUrl.get();
            List<NodeInfoModel.DataBean> dataBeans = SPUtils.getNodeInfo(SPKeyGlobal.CUSTOM_NODE_MODEL_LIST);
            if (null == dataBeans) {
                dataBeans = new ArrayList<>();
            }
            dataBeans.add(dataBean);
            SPUtils.setDataList(SPKeyGlobal.CUSTOM_NODE_MODEL_LIST, dataBeans);
            finish();
        }
    });

}
