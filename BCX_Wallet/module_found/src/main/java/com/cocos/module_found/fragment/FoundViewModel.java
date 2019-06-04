package com.cocos.module_found.fragment;

import android.app.Application;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.entity.FoundListModel;
import com.cocos.library_base.utils.Utils;
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

    private int[] navTitleIds = {R.string.module_found_officel_net, R.string.module_found_block_browser, R.string.module_found_ecology, R.string.module_found_dapp_cooperation};
    private int[] navTitleColorIds = {R.color.color_262A33, R.color.color_262A33, R.color.color_262A33, R.color.color_A5A9B1};
    private int[] navIconIds = {R.drawable.found_nav_office, R.drawable.found_nav_block_brower_icon, R.drawable.found_nav_ecology_icon, R.drawable.found_nav_dapp_cooperation_icon};
    private int[] navUrl = {R.string.module_found_cocosbcx_url, R.string.module_found_explorer_cocosbcx_url, R.string.module_found_community_cocosbcx_url, 0};
    public int[] foundListTitle = {R.string.module_found_shooting, R.string.module_found_lady_luck, R.string.module_found_fruit_machine};
    private int[] foundListDesc = {R.string.module_found_shooting_desc, R.string.module_found_lady_luck_desc, R.string.module_found_dice_game_desc};
    private int[] foundListUrl = {R.string.module_found_shooting_url, R.string.module_found_lady_luck_url, R.string.module_found_dice_game_url};
    private int[] foundListIconId = {R.drawable.found_list_shooting_icon, R.drawable.found_lucky_lady_icon, R.drawable.found_dice_game};


    public FoundViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * grid
     */
    public ObservableList<FoundNavItemViewModel> navObservableList = new ObservableArrayList<>();
    public ItemBinding<FoundNavItemViewModel> navItemBinding = ItemBinding.of(BR.viewModel, R.layout.found_nav_item);
    public final BindingRecyclerViewAdapter<FoundNavItemViewModel> navAdapter = new BindingRecyclerViewAdapter<>();


    public void initNavListData() {
        List<FoundNavModel> navModels = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            FoundNavModel foundNavModel = new FoundNavModel();
            foundNavModel.setNavIconId(navIconIds[i]);
            foundNavModel.setNavTitleColor(navTitleColorIds[i]);
            foundNavModel.setNavTitle(Utils.getString(navTitleIds[i]));
            foundNavModel.setNavUrl(i != 3 ? Utils.getString(navUrl[i]) : "");
            navModels.add(foundNavModel);
        }
        for (FoundNavModel foundNavModel : navModels) {
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


    public void initListData() {
        List<FoundListModel> listModels = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            FoundListModel foundListModel = new FoundListModel();
            foundListModel.setListDesc(Utils.getString(foundListDesc[i]));
            foundListModel.setListTitle(Utils.getString(foundListTitle[i]));
            foundListModel.setListUrl(Utils.getString(foundListUrl[i]));
            foundListModel.setListIcon(foundListIconId[i]);
            listModels.add(foundListModel);
        }
        for (FoundListModel foundlistModel : listModels) {
            FoundListItemViewModel itemViewModel = new FoundListItemViewModel(FoundViewModel.this, foundlistModel);
            listObservableList.add(itemViewModel);
        }
    }
}
