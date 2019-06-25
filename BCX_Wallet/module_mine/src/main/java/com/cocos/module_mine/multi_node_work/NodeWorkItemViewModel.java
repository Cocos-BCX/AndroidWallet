package com.cocos.module_mine.multi_node_work;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.TextUtils;
import android.view.View;

import com.cocos.library_base.base.ItemViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.entity.NodeInfoModel;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.global.SPKeyGlobal;
import com.cocos.library_base.utils.SPUtils;
import com.cocos.library_base.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * @author ningkang.guo
 * @Date 2019/5/30
 */
public class NodeWorkItemViewModel extends ItemViewModel<NodeWorkViewModel> {

    public ObservableField<NodeInfoModel.DataBean> entity = new ObservableField<>();
    private NodeInfoModel.DataBean currentNodeModel;
    public ObservableInt deleteVisible = new ObservableInt(View.GONE);

    /**
     * @param nodeWorkViewModel
     * @param dataBean
     */
    public NodeWorkItemViewModel(NodeWorkViewModel nodeWorkViewModel, NodeInfoModel.DataBean dataBean) {
        super(nodeWorkViewModel);
        this.entity.set(dataBean);
        this.currentNodeModel = dataBean;
        // 默认选中节点
        NodeInfoModel.DataBean selectedNodeModel = SPUtils.getObject(Utils.getContext(), SPKeyGlobal.NODE_WORK_MODEL_SELECTED);
        if (null != selectedNodeModel) {
            nodeCheck.set(TextUtils.equals(currentNodeModel.toString(), selectedNodeModel.toString()));
        }
        if (dataBean.isNative) {
            deleteVisible.set(View.VISIBLE);
        }
    }

    public ObservableBoolean nodeCheck = new ObservableBoolean(false);

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            // 去切换
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.SWITCH_NODE_WORK);
            eventBusCarrier.setObject(currentNodeModel);
            EventBus.getDefault().post(eventBusCarrier);
            nodeCheck.set(true);
        }
    });

    //删除条目
    public BindingCommand deleteClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            List<NodeInfoModel.DataBean> dataBeans = SPUtils.getNodeInfo(SPKeyGlobal.CUSTOM_NODE_MODEL_LIST);
            for (int i = 0; i < dataBeans.size(); i++) {
                if (TextUtils.equals(dataBeans.get(i).toString(), currentNodeModel.toString())) {
                    dataBeans.remove(i);
                    i--;
                }
            }
            SPUtils.setDataList(SPKeyGlobal.CUSTOM_NODE_MODEL_LIST, dataBeans);
            EventBusCarrier eventBusCarrier = new EventBusCarrier();
            eventBusCarrier.setEventType(EventTypeGlobal.DIALOG_DISMISS_TYPE);
            eventBusCarrier.setObject(null);
            EventBus.getDefault().post(eventBusCarrier);
        }
    });


}
