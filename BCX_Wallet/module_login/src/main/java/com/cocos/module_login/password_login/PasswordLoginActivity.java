package com.cocos.module_login.password_login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.library_base.BR;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.entity.BaseResult;
import com.cocos.library_base.entity.NodeInfoModel;
import com.cocos.library_base.global.SPKeyGlobal;
import com.cocos.library_base.http.api.BaseUrlApi;
import com.cocos.library_base.http.callback.BaseObserver;
import com.cocos.library_base.http.http.HttpMethods;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.AccountHelperUtils;
import com.cocos.library_base.utils.SPUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.VersionUtil;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.widget.spinner.CustomSpinner;
import com.cocos.module_login.R;
import com.cocos.module_login.databinding.ActivityPasswordLoginBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * @author ningkang.guo
 * @Date 2019/3/29
 */
@Route(path = RouterActivityPath.ACTIVITY_PASSWORD_LOGIN)
public class PasswordLoginActivity extends BaseActivity<ActivityPasswordLoginBinding, PasswordLoginViewModel> {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_password_login;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void addToContainer() {
        //空实现,不加入ActivityContainer
    }


    @Override
    public void initData() {
        VersionUtil.updateVersion(viewModel, PasswordLoginActivity.this);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        setNodeListData();
        binding.loginNodeSwitch.setOnItemSelectedListener(new CustomSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int pos) {
                try {
                    final NodeInfoModel.DataBean dataBean = (NodeInfoModel.DataBean) arg0.getAdapter().getItem(pos);
                    List<String> nodeUrls = new ArrayList<>();
                    nodeUrls.add(dataBean.ws);
                    nodeUrls.add(dataBean.ws);
                    nodeUrls.add(dataBean.ws);
                    CocosBcxApiWrapper.getBcxInstance().connect(PasswordLoginActivity.this, dataBean.chainId, nodeUrls, dataBean.faucetUrl, dataBean.coreAsset, true, new IBcxCallBack() {
                        @Override
                        public void onReceiveValue(String value) {
                            BaseResult resultEntity = GsonSingleInstance.getGsonInstance().fromJson(value, BaseResult.class);
                            if (!resultEntity.isSuccess()) {
                                ToastUtils.showShort(R.string.module_mine_node_connect_failed);
                                return;
                            }
                            ToastUtils.showShort(R.string.module_mine_node_connect_success);
                            SPUtils.putObject(Utils.getContext(), SPKeyGlobal.NODE_WORK_MODEL_SELECTED, dataBean);
                            SPUtils.putString(Utils.getContext(), SPKeyGlobal.NET_TYPE, dataBean.type);
                            reSetNodeText(dataBean);
                            List<String> accountNames = CocosBcxApiWrapper.getBcxInstance().get_dao_account_names();
                            if (null != accountNames && accountNames.size() > 0) {
                                AccountHelperUtils.setCurrentAccountName(accountNames.get(0));
                            } else {
                                AccountHelperUtils.setCurrentAccountName("");
                            }
                        }
                    });
                } catch (Exception e) {
                }
            }
        });
    }

    /**
     * 请求并加载节点数据
     */
    public void setNodeListData() {
        try {
            final NodeInfoModel.DataBean selectedNodeModel = SPUtils.getObject(Utils.getContext(), SPKeyGlobal.NODE_WORK_MODEL_SELECTED);
            final List<NodeInfoModel.DataBean> dataBeans = SPUtils.getNodeInfo(SPKeyGlobal.CUSTOM_NODE_MODEL_LIST);
            Observable<NodeInfoModel> observable = BaseUrlApi.getApiBaseService().getNodeInfo();
            HttpMethods.toSubscribe(observable, new BaseObserver<NodeInfoModel>() {
                @Override
                protected void onBaseNext(NodeInfoModel data) {
                    if (data.status != 200) {
                        return;
                    }
                    List<NodeInfoModel.DataBean> dataBeanList = new ArrayList<>();
                    for (NodeInfoModel.DataBean dataBean : data.data) {
                        if (!dataBeans.contains(dataBean)) {
                            dataBeanList.add(dataBean);
                        }
                    }
                    dataBeanList.addAll(dataBeans);
                    setDefaultNode(selectedNodeModel, dataBeanList);
                }

                @Override
                protected void onBaseError(Throwable t) {
                    setDefaultNode(selectedNodeModel, dataBeans);
                }
            });
        } catch (Exception e) {
            NodeInfoModel.DataBean selectedNodeModel = SPUtils.getObject(Utils.getContext(), SPKeyGlobal.NODE_WORK_MODEL_SELECTED);
            List<NodeInfoModel.DataBean> dataBeans = SPUtils.getNodeInfo(SPKeyGlobal.CUSTOM_NODE_MODEL_LIST);
            setDefaultNode(selectedNodeModel, dataBeans);
        }
    }

    private void setDefaultNode(NodeInfoModel.DataBean selectedNodeModel, List<NodeInfoModel.DataBean> dataBeans) {
        if (null != selectedNodeModel && !TextUtils.isEmpty(selectedNodeModel.name)) {
            binding.loginNodeSwitch.setDefaultText(selectedNodeModel.name);
        } else {
            binding.loginNodeSwitch.setDefaultText("");
        }
        if (dataBeans != null && dataBeans.size() > 0) {
            binding.loginNodeSwitch.setData(dataBeans);
        }
    }

    private void reSetNodeText(NodeInfoModel.DataBean selectedNodeModel) {
        if (null != selectedNodeModel && !TextUtils.isEmpty(selectedNodeModel.name)) {
            binding.loginNodeSwitch.setDefaultText(selectedNodeModel.name);
        } else {
            binding.loginNodeSwitch.setDefaultText("");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10086) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                boolean hasInstallPermission = getPackageManager().canRequestPackageInstalls();
                if (hasInstallPermission && !TextUtils.isEmpty(VersionUtil.getDestFileDir(PasswordLoginActivity.this))) {
                    VersionUtil.installApkO(PasswordLoginActivity.this, VersionUtil.getDestFileDir(PasswordLoginActivity.this));
                }
            }
        }
    }
}
