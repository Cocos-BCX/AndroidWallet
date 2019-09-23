package com.cocos.module_found.fragment;

import android.app.Application;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.FoundListModel;
import com.cocos.library_base.entity.FoundModel;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.module_found.BR;
import com.cocos.module_found.R;
import com.cocos.module_found.entity.FoundNavModel;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * @author ningkang.guo
 * @Date 2019/2/19
 */
public class FoundViewModel extends BaseViewModel {

    private int[] navTitleColorIds = {R.color.color_262A33, R.color.color_262A33, R.color.color_262A33, R.color.color_A5A9B1};

    public FoundViewModel(@NonNull Application application) {
        super(application);
    }


    /**
     * grid
     */
    public ObservableList<FoundNavItemViewModel> navObservableList = new ObservableArrayList<>();
    public ItemBinding<FoundNavItemViewModel> navItemBinding = ItemBinding.of(BR.viewModel, R.layout.found_nav_item);
    public final BindingRecyclerViewAdapter<FoundNavItemViewModel> navAdapter = new BindingRecyclerViewAdapter<>();

    public void initNavListData(FoundModel data, int selectLanguage) {
        navObservableList.clear();
        List<FoundModel.DataBeanX.NavBean> navModels = data.getData().getNav();
        List<FoundNavModel> navBeans = new ArrayList<>();
        for (int i = 0; i < navModels.size(); i++) {
            FoundModel.DataBeanX.NavBean navBean = navModels.get(i);
            FoundNavModel foundNavModel = new FoundNavModel();
            foundNavModel.setNavIconUrl(navBean.getImageUrl());
            foundNavModel.setNavTitleColor(navTitleColorIds[i]);
            foundNavModel.setNavTitle(selectLanguage == 0 ? navBean.getTitle() : navBean.getEnTitle());
            foundNavModel.setNavUrl(navBean.getLinkUrl());
            navBeans.add(foundNavModel);
        }
        for (FoundNavModel foundNavModel : navBeans) {
            FoundNavItemViewModel itemViewModel = new FoundNavItemViewModel(FoundViewModel.this, foundNavModel);
            navObservableList.add(itemViewModel);
        }
    }

    /**
     * list
     */
    public ObservableList<FoundListItemViewModel> listObservableList = new ObservableArrayList<>();
    public ItemBinding<FoundListItemViewModel> listItemBinding = ItemBinding.of(BR.viewModel, R.layout.module_found_list_item);
    public final BindingRecyclerViewAdapter<FoundListItemViewModel> listAdapter = new BindingRecyclerViewAdapter<>();

    public void initListData(FoundModel data, int selectLanguage) {
        listObservableList.clear();
        List<FoundModel.DataBeanX.ListBean> listBeans = data.getData().getList();
        List<FoundListModel> listModels = new ArrayList<>();
        String headTitle = null;
        for (FoundModel.DataBeanX.ListBean listBean : listBeans) {
            List<FoundModel.DataBeanX.ListBean.DataBean> dataBeans = listBean.getData();
            for (FoundModel.DataBeanX.ListBean.DataBean dataBean : dataBeans) {
                FoundListModel foundListModel = new FoundListModel();
                foundListModel.setListDesc(selectLanguage == 0 ? dataBean.getDec() : dataBean.getEnDec());
                foundListModel.setListTitle(selectLanguage == 0 ? dataBean.getTitle() : dataBean.getEnTitle());
                foundListModel.setLinkUrl(dataBean.getLinkUrl());
                foundListModel.setImageUrl(dataBean.getImageUrl());
                if (!TextUtils.isEmpty(listBean.getHeader()) && !TextUtils.equals(headTitle, listBean.getHeader())) {
                    foundListModel.setHeaderTitle(selectLanguage == 0 ? listBean.getHeader() : listBean.getEnHeader());
                    headTitle = listBean.getHeader();
                }
                listModels.add(foundListModel);
            }
        }
        for (FoundListModel foundListModel : listModels) {
            FoundListItemViewModel itemViewModel = new FoundListItemViewModel(FoundViewModel.this, foundListModel);
            listObservableList.add(itemViewModel);
        }
    }

    /**
     * 跳转搜索
     */
    public BindingCommand searchOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_SEARCH).navigation();
        }
    });
}
