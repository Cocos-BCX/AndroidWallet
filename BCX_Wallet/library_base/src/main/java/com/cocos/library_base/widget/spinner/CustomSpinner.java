package com.cocos.library_base.widget.spinner;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cocos.library_base.R;
import com.cocos.library_base.entity.NodeInfoModel;
import com.cocos.library_base.utils.Utils;

import java.util.List;


public class CustomSpinner extends LinearLayout {

    private TextView tv_value;
    private ImageView bt_dropdown;
    private int mNormalColor;
    private int mSelectedColor;
    private Context mcontext;
    private List<NodeInfoModel.DataBean> mItems;
    OnItemSelectedListener listener;
    private SpinnerPopWindow mSpinerPopWindow;
    private SpinnerAdapter mAdapter;
    View myView;
    private RelativeLayout rl_node_select;


    public CustomSpinner(Context context) {
        super(context);
        mcontext = context;
        init();
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        mcontext = context;
        init();
    }

    private void init() {
        LayoutInflater mInflater = LayoutInflater.from(mcontext);
        myView = mInflater.inflate(R.layout.myspinner_layout, null);
        addView(myView);
        rl_node_select = (RelativeLayout) myView.findViewById(R.id.rl_node_select);
        tv_value = (TextView) myView.findViewById(R.id.tv_value);
        bt_dropdown = (ImageView) myView.findViewById(R.id.bt_dropdown);
        rl_node_select.setOnClickListener(onClickListener);
    }

    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            startPopWindow();
        }
    };

    public void setData(List<NodeInfoModel.DataBean> datas) {
        mItems = datas;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    public void startPopWindow() {
        mAdapter = new SpinnerAdapter(mcontext);
        mAdapter.refreshData(mItems, 0);
        mSpinerPopWindow = new SpinnerPopWindow(mcontext);
        mSpinerPopWindow.setAdatper(mAdapter);
        mSpinerPopWindow.setItemListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int pos) {
                // TODO Auto-generated method stub
                listener.onItemSelected(arg0, view, pos);
            }
        });
        showSpinWindow();
    }

    public void setDefaultText(String text) {
        if (null != tv_value) {
            tv_value.setText(text);
            tv_value.setTextColor(Utils.getColor(R.color.color_4868DC));
        }
    }

    private void showSpinWindow() {
        mSpinerPopWindow.setWidth(myView.getWidth());
        mSpinerPopWindow.showAsDropDown(myView);
    }

    public interface OnItemSelectedListener {
        void onItemSelected(AdapterView<?> arg0, View view, int pos);
    }
}  