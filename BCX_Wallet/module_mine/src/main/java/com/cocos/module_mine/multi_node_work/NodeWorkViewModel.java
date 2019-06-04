package com.cocos.module_mine.multi_node_work;

import android.app.Application;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.entity.NodeInfoModel;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.global.SPKeyGlobal;
import com.cocos.library_base.http.api.BaseUrlApi;
import com.cocos.library_base.http.callback.BaseObserver;
import com.cocos.library_base.http.http.HttpMethods;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.SPUtils;
import com.cocos.module_mine.BR;
import com.cocos.module_mine.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @author ningkang.guo
 * @Date 2019/2/22
 */
public class NodeWorkViewModel extends BaseViewModel {


    public NodeWorkViewModel(@NonNull Application application) {
        super(application);
    }

    //添加节点
    public BindingCommand custom_node = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.DIALOG_DISMISS_TYPE);
            eventBusCarrier.setObject(null);
            EventBus.getDefault().post(eventBusCarrier);
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_CUSTOM_NODE_WORK).navigation();
        }
    });


    public ObservableList<NodeWorkItemViewModel> nodeWorkObservableList = new ObservableArrayList<>();
    public BindingRecyclerViewAdapter<NodeWorkItemViewModel> nodeWorkAdapter = new BindingRecyclerViewAdapter<>();
    public ItemBinding<NodeWorkItemViewModel> nodeWorkItemBinding = ItemBinding.of(BR.viewModel, R.layout.module_mine_item_node_work);

    /**
     * 请求并加载节点数据
     */
    public void requestNodeListData() {
        try {
            nodeWorkObservableList.clear();
            Observable<NodeInfoModel> observable = BaseUrlApi.getApiBaseService().getNodeInfo();
            HttpMethods.toSubscribe(observable, new BaseObserver<NodeInfoModel>() {
                @Override
                protected void onBaseNext(NodeInfoModel data) {
                    List<NodeInfoModel.DataBean> dataBeans = SPUtils.getNodeInfo(SPKeyGlobal.CUSTOM_NODE_MODEL_LIST);
                    if (data.status != 200) {
                        if (null == dataBeans || dataBeans.size() <= 0) {
                            return;
                        }
                        for (NodeInfoModel.DataBean dataBean : dataBeans) {
                            NodeWorkItemViewModel itemViewModel = new NodeWorkItemViewModel(NodeWorkViewModel.this, dataBean);
                            nodeWorkObservableList.add(itemViewModel);
                        }
                        return;
                    }

                    if (null == dataBeans) {
                        dataBeans = new ArrayList<>();
                    }
                    for (NodeInfoModel.DataBean dataBean : data.data) {
                        NodeWorkItemViewModel itemViewModel = new NodeWorkItemViewModel(NodeWorkViewModel.this, dataBean);
                        nodeWorkObservableList.add(itemViewModel);
                    }

                    for (NodeInfoModel.DataBean dataBean : dataBeans) {
                        NodeWorkItemViewModel itemViewModel = new NodeWorkItemViewModel(NodeWorkViewModel.this, dataBean);
                        nodeWorkObservableList.add(itemViewModel);
                    }
                }
            });
        } catch (Exception e) {
            List<NodeInfoModel.DataBean> dataBeans = SPUtils.getNodeInfo(SPKeyGlobal.CUSTOM_NODE_MODEL_LIST);
            if (null == dataBeans || dataBeans.size() <= 0) {
                return;
            }
            for (NodeInfoModel.DataBean dataBean : dataBeans) {
                NodeWorkItemViewModel itemViewModel = new NodeWorkItemViewModel(NodeWorkViewModel.this, dataBean);
                nodeWorkObservableList.add(itemViewModel);
            }
        }
    }
}
