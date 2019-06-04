package com.cocos.module_found.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cocos.library_base.base.BaseFragment;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.entity.FoundListModel;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.utils.StatusBarUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.widget.DialogFragment;
import com.cocos.library_base.widget.image_slide.ImageSlideshow;
import com.cocos.module_found.BR;
import com.cocos.module_found.R;
import com.cocos.module_found.databinding.FragmentFoundBinding;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by guoningkang on 2019/2/12.
 */

public class FoundFragment extends BaseFragment<FragmentFoundBinding, FoundViewModel> {

    public int[] foundBanner = {R.drawable.found_vp_shooting, R.drawable.found_vp_luck_lady_banner};
    public int[] foundBannerUrl = {R.string.module_found_shooting_url, R.string.module_found_lady_luck_url};

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_found;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        int statusHeight = StatusBarUtils.getStatusBarHeight(getActivity());
        binding.foundTitle.setPadding(0, statusHeight, 0, 0);
        initVpData();
        viewModel.initNavListData();
        viewModel.initListData();
    }

    private void initVpData() {
        for (int i = 0; i < foundBanner.length; i++) {
            binding.vpFound.addImageResource(foundBanner[i]);
        }
        binding.vpFound.setDelay(3000);
        binding.vpFound.setOnItemClickListener(new ImageSlideshow.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FoundListModel foundListMode = new FoundListModel();
                foundListMode.setListUrl(Utils.getString(foundBannerUrl[position]));
                foundListMode.setListTitle(Utils.getString(viewModel.foundListTitle[position]));
                EventBusCarrier eventBusCarrier = new EventBusCarrier();
                eventBusCarrier.setEventType(EventTypeGlobal.SHOW_FOUND_DIALOG);
                eventBusCarrier.setObject(foundListMode);
                EventBus.getDefault().post(eventBusCarrier);
            }
        });
        binding.vpFound.commit();
    }

    @Override
    protected void initHandlerEvent(EventBusCarrier busCarrier) {
        if (TextUtils.equals(EventTypeGlobal.SHOW_FOUND_DIALOG, busCarrier.getEventType())) {
            FoundListModel foundlistModel = (FoundListModel) busCarrier.getObject();
            DialogFragment dialogFragment = new DialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString(IntentKeyGlobal.DIALOG_CONTENT, Utils.getString(R.string.module_found_found_dapp_dialog_text));
            bundle.putInt(IntentKeyGlobal.DIALOG_TYPE, 2);
            bundle.putSerializable(IntentKeyGlobal.WEB_MODEL, foundlistModel);
            dialogFragment.setArguments(bundle);
            dialogFragment.show(getFragmentManager(), "dialogFragment");
        }
    }

    @Override
    public void onDestroy() {
        // 释放资源
        binding.vpFound.releaseResource();
        super.onDestroy();
    }
}
