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
import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.library_base.base.BaseActivity;
import com.cocos.library_base.entity.WebViewModel;
import com.cocos.library_base.global.IntentKeyGlobal;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;


/**
 * @author ningkang.guo
 * @Date 2019/6/14
 */
@Route(path = RouterActivityPath.ACTIVITY_SEARCH)
public class SearchActivity extends BaseActivity<ActivitySearchBinding, SearchViewModel> {


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
        binding.tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.remove(SearchActivity.this, SPKeyGlobal.SEARCH_INFO);
                binding.llEmpty.setVisibility(View.VISIBLE);
                binding.llHistory.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSearchData();
    }

    private void setSearchData() {
        try {
            LinkedHashMap<String, String> searchInfo = SPUtils.getMap(SPKeyGlobal.SEARCH_INFO);
            final List<String> mTitles = new ArrayList<>();
            final List<String> mUrls = new ArrayList<>();
            if (null == searchInfo || searchInfo.size() <= 0) {
                binding.llEmpty.setVisibility(View.VISIBLE);
                binding.llHistory.setVisibility(View.INVISIBLE);
                return;
            }
            ListIterator<Map.Entry<String, String>> i = new ArrayList<>(searchInfo.entrySet()).listIterator(searchInfo.size());
            while (i.hasPrevious()) {
                Map.Entry<String, String> entry = i.previous();
                mTitles.add(entry.getValue());
                mUrls.add(entry.getKey());
            }
            binding.llEmpty.setVisibility(View.INVISIBLE);
            binding.llHistory.setVisibility(View.VISIBLE);
            final LayoutInflater mInflater = LayoutInflater.from(SearchActivity.this);
            binding.idFlowlayout.setAdapter(new TagAdapter<String>(mTitles) {

                @Override
                public View getView(FlowLayout parent, final int position, String s) {
                    try {
                        TextView view = (TextView) mInflater.inflate(R.layout.search_item, binding.idFlowlayout, false);
                        view.setText(s);
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
                    WebViewModel webViewModel = new WebViewModel();
                    webViewModel.setUrl(mUrls.get(position));
                    webViewModel.setTitle(mTitles.get(position));
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(IntentKeyGlobal.WEB_MODEL, webViewModel);
                    bundle.putBoolean(IntentKeyGlobal.FROM_SEARCH, true);
                    ARouter.getInstance().build(RouterActivityPath.ACTIVITY_JS_WEB).with(bundle).navigation();
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
