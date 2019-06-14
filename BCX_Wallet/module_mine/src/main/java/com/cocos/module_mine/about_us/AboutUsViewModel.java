package com.cocos.module_mine.about_us;

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
import com.cocos.library_base.utils.AppApplicationMgr;
import com.cocos.library_base.utils.Utils;
import com.cocos.module_mine.R;

/**
 * @author ningkang.guo
 * @Date 2019/2/22
 */
public class AboutUsViewModel extends BaseViewModel {

    public AboutUsViewModel(@NonNull Application application) {
        super(application);
        currentVersion.set(Utils.getString(R.string.module_mine_current_version) + AppApplicationMgr.getVersionName(Utils.getContext()));
    }

    public ObservableField<String> currentVersion = new ObservableField<>();

    public BindingCommand backOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    public BindingCommand useProtocolOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            WebViewModel webViewModel = new WebViewModel();
            webViewModel.setTitle(Utils.getString(R.string.fragment_mine_about_us_use_protocol));
            webViewModel.setUrl(Utils.getString(R.string.fragment_mine_about_us_privacy_policy_url));
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKeyGlobal.WEB_MODEL, webViewModel);
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_HTML_WEB).with(bundle).navigation();
        }
    });

    public BindingCommand privacyProtocolOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            WebViewModel webViewModel = new WebViewModel();
            webViewModel.setTitle(Utils.getString(R.string.fragment_mine_about_us_privacy_protocol));
            webViewModel.setUrl(Utils.getString(R.string.fragment_mine_about_us_terms_of_service_url));
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKeyGlobal.WEB_MODEL, webViewModel);
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_HTML_WEB).with(bundle).navigation();
        }
    });

    public BindingCommand versionLogOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            WebViewModel webViewModel = new WebViewModel();
            webViewModel.setTitle(Utils.getString(R.string.fragment_mine_about_us_version_log));
            webViewModel.setUrl(Utils.getString(R.string.fragment_mine_about_us_version_log_url));
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKeyGlobal.WEB_MODEL, webViewModel);
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_HTML_WEB).with(bundle).navigation();
        }
    });

    public BindingCommand joinCommunityOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ARouter.getInstance().build(RouterActivityPath.ACTIVITY_JOIN_COMMUNITY).navigation();
        }
    });


}
