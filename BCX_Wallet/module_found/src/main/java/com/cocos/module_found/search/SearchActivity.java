package com.cocos.module_found.search;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.global.SPKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.SPUtils;
import com.cocos.module_found.BR;
import com.cocos.module_found.R;
import com.cocos.module_found.databinding.ActivitySearchBinding;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author ningkang.guo
 * @Date 2019/6/14
 */
@Route(path = RouterActivityPath.ACTIVITY_SEARCH)
public class SearchActivity extends BaseActivity<ActivitySearchBinding, SearchViewModel> {

    private Map<String, String> searchInfo;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_search;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        setSearchData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSearchData();
    }

    private void setSearchData() {
        try {
            searchInfo = SPUtils.getMap(SPKeyGlobal.SEARCH_INFO);
            final List<String> mTitles = new ArrayList<>();
            final List<String> mUrls = new ArrayList<>();
            if (null == searchInfo || searchInfo.size() <= 0) {
                binding.llEmpty.setVisibility(View.VISIBLE);
                binding.idFlowlayout.setVisibility(View.INVISIBLE);
                return;
            }
            for (Map.Entry<String, String> entry : searchInfo.entrySet()) {
                mTitles.add(entry.getKey());
                mUrls.add(entry.getValue());
            }
            binding.llEmpty.setVisibility(View.INVISIBLE);
            binding.idFlowlayout.setVisibility(View.VISIBLE);

            final LayoutInflater mInflater = LayoutInflater.from(SearchActivity.this);
            binding.idFlowlayout.setAdapter(new TagAdapter<String>(mTitles) {

                @Override
                public View getView(FlowLayout parent, final int position, String s) {
                    try {
                        View view = mInflater.inflate(R.layout.search_item, binding.idFlowlayout, false);
                        TextView textView = view.findViewById(R.id.tv_text);
                        textView.setText(s);
                        return view;
                    } catch (Exception e) {
                    }
                    return null;
                }
            });

            binding.idFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
                    binding.edtSearchContent.setText(mUrls.get(position));
                    return true;
                }
            });

            binding.idFlowlayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
                @Override
                public void onSelected(Set<Integer> selectPosSet) {
                }
            });
        } catch (Exception e) {
        }
    }

}
