package com.cocos.library_base.utils.node;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.bcx_sdk.bcx_log.LogUtils;
import com.cocos.library_base.R;
import com.cocos.library_base.entity.BaseResult;
import com.cocos.library_base.entity.NodeInfoModel;
import com.cocos.library_base.global.SPKeyGlobal;
import com.cocos.library_base.http.api.BaseUrlApi;
import com.cocos.library_base.http.callback.BaseObserver;
import com.cocos.library_base.http.http.HttpMethods;
import com.cocos.library_base.utils.SPUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * @author ningkang.guo
 * @Date 2019/11/13
 */
public class NodeConnectUtil {


    public static void testNetStatus() {
        try {
            NodeInfoModel.DataBean selectedNodeModel = SPUtils.getObject(Utils.getContext(), SPKeyGlobal.NODE_WORK_MODEL_SELECTED);
            if (null != selectedNodeModel) {
                // 初始化bcx节点连接
                List<String> nodeUrls = new ArrayList<>();
                nodeUrls.add(selectedNodeModel.ws);
                nodeUrls.add(selectedNodeModel.ws);
                nodeUrls.add(selectedNodeModel.ws);
                CocosBcxApiWrapper.getBcxInstance().connect(Utils.getContext(), selectedNodeModel.chainId, nodeUrls, selectedNodeModel.faucetUrl, selectedNodeModel.coreAsset, true, s -> {
                });
            }
        } catch (Exception e) {
        }
    }


    /**
     * 请求并加载节点数据
     */
    public static void requestNodeListData(Context context) {
        try {

            Observable<NodeInfoModel> observable = BaseUrlApi.getApiBaseService().getNodeInfo();
            HttpMethods.toSubscribe(observable, new BaseObserver<NodeInfoModel>() {
                @Override
                protected void onBaseNext(NodeInfoModel data) {
                    NodeInfoModel.DataBean selectedNodeModel = SPUtils.getObject(Utils.getContext(), SPKeyGlobal.NODE_WORK_MODEL_SELECTED);
                    boolean is_first_connect = SPUtils.getBoolean(context, SPKeyGlobal.IS_FIRST_CONNECT, true);
                    Log.d("请求节点===", data.data.toString() + "");
                    if (data.status != 200) {
                        onErrorInit(selectedNodeModel, context);
                        return;
                    }
                    for (NodeInfoModel.DataBean dataBean : data.data) {
                        if (dataBean.isForce) {
                            init(dataBean, s -> {
                                BaseResult resultEntity = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResult.class);
                                if (resultEntity.isSuccess()) {
                                    SPUtils.putObject(context, SPKeyGlobal.NODE_WORK_MODEL_SELECTED, dataBean);
                                    SPUtils.putString(context, SPKeyGlobal.NET_TYPE, dataBean.type);
                                    Log.i("init_node_connect---1", s + ":" + dataBean.ws);
                                } else {
                                    ToastUtils.showShort(Utils.getString(R.string.module_mine_node_connect_failed));
                                }
                            }, context);
                            return;
                        } else {
                            // 之前无选中的节点
                            if (null == selectedNodeModel || is_first_connect) {
                                init(dataBean, s -> {
                                    BaseResult resultEntity = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResult.class);
                                    if (resultEntity.isSuccess()) {
                                        SPUtils.putObject(context, SPKeyGlobal.NODE_WORK_MODEL_SELECTED, dataBean);
                                        SPUtils.putString(context, SPKeyGlobal.NET_TYPE, dataBean.type);
                                        SPUtils.putBoolean(context, SPKeyGlobal.IS_FIRST_CONNECT, false);
                                        Log.i("init_node_connect---1", s + ":" + dataBean.ws);
                                    } else {
                                        ToastUtils.showShort(Utils.getString(R.string.module_mine_node_connect_failed));
                                    }
                                }, context);
                                return;
                            } else {
                                // 之前有选中的节点
                                init(selectedNodeModel, s -> {
                                    BaseResult resultEntity = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResult.class);
                                    if (resultEntity.isSuccess()) {
                                        SPUtils.putObject(context, SPKeyGlobal.NODE_WORK_MODEL_SELECTED, selectedNodeModel);
                                        SPUtils.putString(context, SPKeyGlobal.NET_TYPE, selectedNodeModel.type);
                                        Log.i("init_node_connect---2", s + ":" + selectedNodeModel.ws);

                                    } else {
                                        ToastUtils.showShort(Utils.getString(R.string.module_mine_node_connect_failed));
                                    }
                                }, context);
                            }
                        }
                    }
                }

                @Override
                protected void onBaseError(Throwable t) {
                    NodeInfoModel.DataBean selectedNodeModel = SPUtils.getObject(Utils.getContext(), SPKeyGlobal.NODE_WORK_MODEL_SELECTED);
                    onErrorInit(selectedNodeModel, context);
                    Log.d("请求节点", t.getMessage() + "");
                }
            });
        } catch (Exception e) {
            NodeInfoModel.DataBean selectedNodeModel = SPUtils.getObject(Utils.getContext(), SPKeyGlobal.NODE_WORK_MODEL_SELECTED);
            onErrorInit(selectedNodeModel, context);
        }
    }


    /**
     * 连接错误/失败/出现异常时的初始化方法
     */
    private static void onErrorInit(NodeInfoModel.DataBean selectedNodeModel, Context context) {
        if (null == selectedNodeModel) {
            init(new NodeInfoModel.DataBean(), s -> {
                BaseResult resultEntity = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResult.class);
                resultEntity.isSuccess();
            }, context);
            return;
        }
        init(selectedNodeModel, s -> {
            BaseResult resultEntity = GsonSingleInstance.getGsonInstance().fromJson(s, BaseResult.class);
            if (resultEntity.isSuccess()) {
                SPUtils.putObject(Utils.getContext(), SPKeyGlobal.NODE_WORK_MODEL_SELECTED, selectedNodeModel);
                SPUtils.putString(Utils.getContext(), SPKeyGlobal.NET_TYPE, selectedNodeModel.type);
                LogUtils.i("init_node_connect---4", s + ":" + selectedNodeModel.ws);
            }
        }, context);
    }

    /**
     * 切换节点
     */
    private static boolean canT = true;
    private static void init(final NodeInfoModel.DataBean dataBean, IBcxCallBack iBcxCallBack, Context context) {
        // 初始化bcx节点连接|
        Log.d("请求数据处理==","桑少丽");
        List<String> nodeUrls = new ArrayList<>();
        nodeUrls.add(dataBean.ws);
        nodeUrls.add(dataBean.ws);
        nodeUrls.add(dataBean.ws);
        CocosBcxApiWrapper.getBcxInstance().connect(Utils.getContext(), dataBean.chainId, nodeUrls, dataBean.faucetUrl, dataBean.coreAsset, true, iBcxCallBack);

        if (canT) {
            SPUtils.putBoolean(context, "loadComplete", true);
            EventBus.getDefault().post(new Object());
            canT = false;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                canT = true;
            }
        },2000);
    }
}
