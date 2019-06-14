package com.cocos.module_found.fragment;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cocos.library_base.base.BaseFragment;
import com.cocos.library_base.bus.event.EventBusCarrier;
import com.cocos.library_base.entity.FoundListModel;
import com.cocos.library_base.entity.FoundModel;
import com.cocos.library_base.global.EventTypeGlobal;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.http.api.BaseUrlApi;
import com.cocos.library_base.http.callback.BaseObserver;
import com.cocos.library_base.http.http.HttpMethods;
import com.cocos.library_base.receiver.NetStateChangeObserver;
import com.cocos.library_base.receiver.NetStateChangeReceiver;
import com.cocos.library_base.utils.StatusBarUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.library_base.utils.multi_language.SPUtil;
import com.cocos.library_base.widget.DialogFragment;
import com.cocos.library_base.widget.image_slide.ImageSlideshow;
import com.cocos.module_found.BR;
import com.cocos.module_found.R;
import com.cocos.module_found.databinding.FragmentFoundBinding;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by guoningkang on 2019/2/12.
 */

public class FoundFragment extends BaseFragment<FragmentFoundBinding, FoundViewModel> implements NetStateChangeObserver {

    private boolean isRegistered = false;
    private NetStateChangeReceiver changeReceiver;

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
        changeReceiver = new NetStateChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        if (null != getActivity()) {
            getActivity().registerReceiver(changeReceiver, filter);
            isRegistered = true;
            changeReceiver.setNetStateChangeObserver(this);
        }
        int statusHeight = StatusBarUtils.getStatusBarHeight(getActivity());
        binding.foundTitle.setPadding(0, statusHeight, 0, 0);
        int selectLanguage = SPUtil.getInstance(Utils.getContext()).getSelectLanguage();
        loadData(selectLanguage);
    }

    @Override
    public void onNetDisconnected() {

    }

    @Override
    public void onNetConnected() {
        int selectLanguage = SPUtil.getInstance(Utils.getContext()).getSelectLanguage();
        loadData(selectLanguage);
    }

    private void loadData(final int selectLanguage) {
        Observable<FoundModel> observable = BaseUrlApi.getApiBaseService().getFoundInfo();
        HttpMethods.toSubscribe(observable, new BaseObserver<FoundModel>() {
            @Override
            protected void onBaseNext(FoundModel data) {
                final List<FoundModel.DataBeanX.BannerBean> bannerBeans = data.getData().getBanner();
                binding.vpFound.clear();
                for (FoundModel.DataBeanX.BannerBean bannerBean : bannerBeans) {
                    binding.vpFound.addImageUrl(bannerBean.getImageUrl());
                }
                binding.vpFound.setOnItemClickListener(new ImageSlideshow.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        FoundModel.DataBeanX.BannerBean bannerBean = bannerBeans.get(position);
                        FoundListModel foundListMode = new FoundListModel();
                        foundListMode.setLinkUrl(bannerBean.getLinkUrl());
                        foundListMode.setListTitle(selectLanguage == 0 ? bannerBean.getTitle() : bannerBean.getEnTitle());
                        EventBusCarrier eventBusCarrier = new EventBusCarrier();
                        eventBusCarrier.setEventType(EventTypeGlobal.SHOW_FOUND_DIALOG);
                        eventBusCarrier.setObject(foundListMode);
                        EventBus.getDefault().post(eventBusCarrier);
                    }
                });
                binding.vpFound.commit();
                binding.vpFound.setDelay(3000);
                viewModel.initNavListData(data, selectLanguage);
                viewModel.initListData(data, selectLanguage);
                binding.tvHotTitle.setText(selectLanguage == 0 ? data.getData().getList().get(0).getHeader() : data.getData().getList().get(0).getEnHeader());
            }
        });

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
        if (isRegistered && null != getActivity()) {
            getActivity().unregisterReceiver(changeReceiver);
        }
        // 释放资源
        binding.vpFound.releaseResource();
        super.onDestroy();
    }
}
