package com.cocos.module_found.fragment;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.base.ItemViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;

/**
 * @author ningkang.guo
 * @Date 2019/2/27
 */
public class FoundViewPagerItemViewModel extends ItemViewModel {

    public Drawable vpBanner;

    public BindingCommand onItemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });

    public FoundViewPagerItemViewModel(@NonNull BaseViewModel viewModel, int bannerId) {
        super(viewModel);
        vpBanner = ContextCompat.getDrawable(viewModel.getApplication(), bannerId);
    }
}
