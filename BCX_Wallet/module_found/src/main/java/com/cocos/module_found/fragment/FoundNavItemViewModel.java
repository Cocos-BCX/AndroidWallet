package com.cocos.module_found.fragment;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.base.ItemViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.WebViewModel;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.module_found.R;
import com.cocos.module_found.entity.FoundNavModel;

/**
 * @author ningkang.guo
 * @Date 2019/2/27
 */
public class FoundNavItemViewModel extends ItemViewModel {

    public ObservableField<String> navTitle = new ObservableField<>();
    public ObservableInt navTitleColor = new ObservableInt(Utils.getColor(R.color.color_262A33));
    public Drawable navIcon;
    FoundNavModel foundNavModel;

    public FoundNavItemViewModel(@NonNull BaseViewModel viewModel, FoundNavModel foundNavModel) {
        super(viewModel);
        this.foundNavModel = foundNavModel;
        navTitle.set(foundNavModel.getNavTitle());
        navTitleColor.set(Utils.getColor(foundNavModel.getNavTitleColor()));
        navIcon = ContextCompat.getDrawable(viewModel.getApplication(), foundNavModel.getNavIconId());
    }

    public BindingCommand onItemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (TextUtils.isEmpty(foundNavModel.getNavUrl())) {
                ToastUtils.showShort(R.string.module_found_to_be_expected);
                return;
            }
            WebViewModel webViewModel = new WebViewModel();
            webViewModel.setTitle(foundNavModel.getNavTitle());
            webViewModel.setUrl(foundNavModel.getNavUrl());
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKeyGlobal.WEB_MODEL, webViewModel);
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_HTML_WEB).with(bundle).navigation();
        }
    });

}
