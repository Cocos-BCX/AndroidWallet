package com.cocos.module_found.search;

import android.app.Application;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.binding.command.BindingAction;
import com.cocos.library_base.binding.command.BindingCommand;
import com.cocos.library_base.entity.WebViewModel;
import com.cocos.library_base.global.IntentKeyGlobal;
import com.cocos.library_base.router.RouterActivityPath;
import com.cocos.library_base.utils.RegexUtils;
import com.cocos.library_base.utils.ToastUtils;
import com.cocos.library_base.utils.Utils;
import com.cocos.module_found.R;

/**
 * @author ningkang.guo
 */
public class SearchViewModel extends BaseViewModel {

    public SearchViewModel(@NonNull Application application) {
        super(application);
    }

    public ObservableField<String> linkUrl = new ObservableField<>();

    //返回按钮事件
    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });


    public BindingCommand confirmOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (!RegexUtils.isURL(linkUrl.get())) {
                ToastUtils.showShort(Utils.getString(R.string.module_found_illegal_dapp_link_url));
                return;
            }
            WebViewModel webViewModel = new WebViewModel();
            webViewModel.setTitle("");
            webViewModel.setUrl(linkUrl.get());
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKeyGlobal.WEB_MODEL, webViewModel);
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_JS_WEB).with(bundle).navigation();
        }
    });

}
