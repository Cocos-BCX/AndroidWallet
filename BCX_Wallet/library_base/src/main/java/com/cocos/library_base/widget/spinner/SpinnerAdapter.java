package com.cocos.library_base.widget.spinner;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cocos.library_base.R;
import com.cocos.library_base.entity.NodeInfoModel;
import com.cocos.library_base.global.SPKeyGlobal;
import com.cocos.library_base.utils.SPUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class SpinnerAdapter<String> extends BaseAdapter {

    private NodeInfoModel.DataBean selectedNodeModel;

    public static interface IOnItemSelectListener {
        public void onItemClick(int pos);
    }

    private Context mContext;
    private List<NodeInfoModel.DataBean> mObjects = new ArrayList<NodeInfoModel.DataBean>();
    private int mSelectItem = 0;

    private LayoutInflater mInflater;

    public SpinnerAdapter(Context context) {
        init(context);
    }

    public void refreshData(List<NodeInfoModel.DataBean> objects, int selIndex) {
        mObjects = objects;
        if (null == mObjects || mObjects.size() <= 0) {
            ToastUtils.showShort(Utils.getString(R.string.node_data_load_failed));
            return;
        }
        if (selIndex < 0) {
            selIndex = 0;
        }
        if (selIndex >= mObjects.size()) {
            selIndex = mObjects.size() - 1;
        }
        mSelectItem = selIndex;
    }

    private void init(Context context) {
        mContext = context;
        selectedNodeModel = SPUtils.getObject(Utils.getContext(), SPKeyGlobal.NODE_WORK_MODEL_SELECTED);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public NodeInfoModel.DataBean getItem(int pos) {
        return mObjects.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup arg2) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.spiner_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NodeInfoModel.DataBean item = getItem(pos);
        viewHolder.mTextView.setText(item.name);
        if (TextUtils.equals(item.toString(), selectedNodeModel.toString())) {
            viewHolder.mTextView.setTextColor(Utils.getColor(R.color.color_4868DC));
        } else {
            viewHolder.mTextView.setTextColor(Utils.getColor(R.color.color_666666));
        }
        return convertView;
    }

    public static class ViewHolder {
        public TextView mTextView;
    }

}
